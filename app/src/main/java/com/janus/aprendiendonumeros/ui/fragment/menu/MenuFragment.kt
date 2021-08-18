package com.janus.aprendiendonumeros.ui.fragment.menu

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource
import com.janus.aprendiendonumeros.databinding.FragmentMenuBinding
import com.janus.aprendiendonumeros.presentation.AuthViewModel
import com.janus.aprendiendonumeros.presentation.AuthViewModelFactory
import com.janus.aprendiendonumeros.repository.auth.AuthImpl
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.NotifyQuestionListener
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl

class MenuFragment : BaseFragment(R.layout.fragment_menu), NotifyQuestionListener {

    private lateinit var binding: FragmentMenuBinding
    private val args: MenuFragmentArgs by navArgs()
    private var navigationAction: Int = 0
    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthImpl(
                AuthDataSource()
            )
        )
    }

    companion object {
        private const val IMAGE_DEFAULT_MALE: Int = R.drawable.ic_photo_profile_boy_deselected
        private const val IMAGE_DEFAULT_FEMALE: Int = R.drawable.ic_photo_profile_girl_deselected
    }

    override fun initUI(view: View) {
        binding = FragmentMenuBinding.bind(view)
        mContext.setQuestionTarget(this)

        val iconsMenu: List<String> = listOf(
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_know_numbers.png?alt=media&token=e405e492-5b6a-41f1-aeca-6aff59db125d",
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce",
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce",
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce",
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce"
        )
        val itemBtnKnowNumbers: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[0],
            position = 0,
            status = MenuItemView.STATUS_EMPTY,
            destination = R.id.action_menu_to_knowNumbers
        )
        val itemBtnSelectAndCount: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[1],
            position = 1,
            status = MenuItemView.STATUS_EMPTY,
            destination = R.id.action_menu_to_howMany
        )
        val itemBtnMoveAndCount: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[2],
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_moveAndCount
        )
        val itemBtnHowMany: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[3],
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_howMany
        )
        val itemBtnLessOrMore: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[4],
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_lessOrMore
        )
        val itemBtnOrderAndCount: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[5],
            position = 2,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_orderAndCount
        )
        val itemBtnOrderAndCount2: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[5],
            position = 2,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_orderAndCount
        )
        val itemBtnOrderAndCount3: MenuItemView = createItemMenu(
            name = "",
            icon = iconsMenu[5],
            position = 2,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_orderAndCount
        )

        binding.contentItemMenu.addView(itemBtnKnowNumbers)
        binding.contentItemMenu.addView(itemBtnSelectAndCount)
        binding.contentItemMenu.addView(itemBtnMoveAndCount)
        binding.contentItemMenu.addView(itemBtnHowMany)
        binding.contentItemMenu.addView(itemBtnLessOrMore)
        binding.contentItemMenu.addView(itemBtnOrderAndCount)
        binding.contentItemMenu.addView(itemBtnOrderAndCount2)
        binding.contentItemMenu.addView(itemBtnOrderAndCount3)

        getData()
        addEvents()
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
        binding.btnExit.setOnClickListener { }
    }

    private fun getData() {
        val userId: String = args.userId
        authViewModel.getUser(userId).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.containerProgressBar.progressBar.visibility =
                    View.VISIBLE
                is Resource.Success -> resultSuccess(result.data)
                is Resource.Failure -> resultFailure(result.exception)
            }
        })
    }

    private fun resultFailure(exception: Exception) {
        binding.containerProgressBar.progressBar.visibility = View.GONE
    }

    private fun resultSuccess(user: User) {
        binding.containerProgressBar.progressBar.visibility = View.GONE
        setupData(user)
    }

    private fun setupData(user: User) {
        val defaultImage: Int = when (user.gender) {
            Constant.GENDER_MALE -> IMAGE_DEFAULT_MALE
            else -> IMAGE_DEFAULT_FEMALE
        }

        binding.civUserProfile.loadImageFromUrl(user.image, defaultImage)
        binding.tvCoin.text = user.coins.toString()
    }

    private fun createItemMenu(
        name: String,
        icon: String,
        position: Int,
        status: Int,
        @IdRes destination: Int,
    ): MenuItemView {
        val item = MenuItemView(requireContext())
        item.getIcon().loadImageFromUrl(icon)
        item.setPosition(position)
        item.setStatus(status)

        item.setClickListener { goTo(destination) }
        return item
    }

    override fun update(answer: Boolean) {
        if (answer && navigationAction != 0) {
            goTo(navigationAction)
        }
    }
}


// EndOfExerciseDialog