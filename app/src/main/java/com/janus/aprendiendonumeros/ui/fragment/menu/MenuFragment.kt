package com.janus.aprendiendonumeros.ui.fragment.menu

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.data.remote.ExerciseDataSource
import com.janus.aprendiendonumeros.databinding.FragmentMenuBinding
import com.janus.aprendiendonumeros.domain.auth.AuthImpl
import com.janus.aprendiendonumeros.domain.exercise.ExerciseImpl
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.presentation.ExerciseViewModel
import com.janus.aprendiendonumeros.presentation.ExerciseViewModelFactory
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog
import com.janus.aprendiendonumeros.ui.listener.NotifyQuestionListener
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import kotlin.system.exitProcess

class MenuFragment : BaseFragment(R.layout.fragment_menu), NotifyQuestionListener {

    private lateinit var binding: FragmentMenuBinding
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val args: MenuFragmentArgs by navArgs()
    private var navigationAction: Int = 0
    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(AuthDataSource())
        )
    }

    private val exerciseViewModel by viewModels<ExerciseViewModel> {
        ExerciseViewModelFactory(
            ExerciseImpl(ExerciseDataSource())
        )
    }

    override fun initUI(view: View) {
        binding = FragmentMenuBinding.bind(view)
        mContext.setQuestionTarget(this)
        addEvents()
    }

    override fun onStart() {
        super.onStart()
        if (binding.contentItemMenu.childCount == 0) {
            binding.contentItemMenu.post {
                getUserData()
            }
        }
    }

    private fun addEvents() {
        binding.civUserProfile.setOnClickListener {
            navigationAction = R.id.action_menu_to_userProfile
            mContext.showDialogConfirmBeingAdult()
        }
        binding.btnSettings.setOnClickListener {
            navigationAction = R.id.action_menu_to_settings
            mContext.showDialogConfirmBeingAdult()
        }
        binding.btnExit.setOnClickListener { exitProcess(0) }
    }

    private fun getUserData() {
        val userId: String = args.userId
        authViewModel.getUser(userId).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Opteniendo datos del usuario...")
                is Resource.Success -> getExerciseData(result.data)
                is Resource.Failure -> resultFailure(result.exception)
            }
        })
    }

    private fun getExerciseData(user: User) {
        exerciseViewModel.getAllExercises(user.id).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.setText("Opteniendo datos del menú...")
                is Resource.Success -> initMenu(user, result.data)
                is Resource.Failure -> resultFailure(result.exception)
            }
        })
    }

    private fun initMenu(user: User, exercises: List<Exercise>) {
        loadingDialog.dismiss()

        binding.civUserProfile.loadImageFromUrl(user.image)
        binding.tvCoin.text = user.coins.toString()

        exercises.forEach { exercise ->
            val icon = when {
                exercise.status != MenuItemView.STATUS_LOCKED -> exercise.image
                else -> R.drawable.bg_item_menu_button_locked
            }

            val itemMenu: MenuItemView = createItemMenu(
                name = exercise.name,
                icon = icon,
                position = exercise.position,
                status = exercise.status,
                navDirections = getNavDirection(exercise.name)
            )
            binding.contentItemMenu.addView(itemMenu)
        }
        binding.contentScroll.visibility = View.VISIBLE
    }

    private fun resultFailure(exception: Exception) {
        loadingDialog.dismiss()
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_ERROR,
            title = "Error al traer los datos",
            text = "Error: ${exception.message}"
        )
    }

    private fun createItemMenu(
        name: String,
        icon: Any,
        position: Int,
        status: Int,
        navDirections: NavDirections?,
    ): MenuItemView {
        return MenuItemView(requireContext()).apply {
            setName(name)
            setPosition(position)
            setStatus(status)

            if (navDirections != null) {
                setClickListener { goTo(navDirections) }
            }
            when (icon) {
                is String -> setImage(icon)
                is Int -> setImage(icon)
            }
        }
    }

    private fun getNavDirection(name: String): NavDirections? {
        return when (name) {
            Exercise.NAME_KNOW_NUMBERS -> MenuFragmentDirections.actionMenuToKnowNumbers(args.userId)
            Exercise.NAME_SELECT_AND_COUNT -> MenuFragmentDirections.actionMenuToSelectAndCount(args.userId)
            Exercise.NAME_MOVE_AND_COUNT -> MenuFragmentDirections.actionMenuToMoveAndCount(args.userId)
            Exercise.NAME_HOW_MANY -> MenuFragmentDirections.actionMenuToHowMany(args.userId)
            Exercise.NAME_LESS_OR_MORE -> MenuFragmentDirections.actionMenuToLessOrMore(args.userId)
            Exercise.NAME_ORDER_AND_COUNT -> MenuFragmentDirections.actionMenuToOrderAndCount(args.userId)
            else -> null
        }
    }

    override fun update(answer: Boolean) {
        if (answer && navigationAction != 0) {
            goTo(navigationAction)
        }
    }
}