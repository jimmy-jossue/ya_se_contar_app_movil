package com.janus.aprendiendonumeros.ui.fragment.informative

import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.remote.LogExerciseDataSource
import com.janus.aprendiendonumeros.data.remote.UserDataSource
import com.janus.aprendiendonumeros.databinding.FragmentEndOfExerciseBinding
import com.janus.aprendiendonumeros.domain.logexercise.LogExerciseImpl
import com.janus.aprendiendonumeros.domain.user.UserImpl
import com.janus.aprendiendonumeros.presentation.LogExerciseViewModel
import com.janus.aprendiendonumeros.presentation.LogExerciseViewModelFactory
import com.janus.aprendiendonumeros.presentation.UserViewModel
import com.janus.aprendiendonumeros.presentation.UserViewModelFactory
import com.janus.aprendiendonumeros.ui.animation.UIAnimations
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog
import com.janus.aprendiendonumeros.ui.utilities.SetSound
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.isGreaterThan
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EndOfExerciseFragment : BaseFragment(R.layout.fragment_end_of_exercise) {

    private lateinit var binding: FragmentEndOfExerciseBinding
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val args: EndOfExerciseFragmentArgs by navArgs()
    private lateinit var log: LogExercise
    private var userId: String = ""
    private var coins: Int = 0
    private var nextExercise: String = ""
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private val setSounds: SetSound by lazy { SetSound(requireContext()) }
    private var soundCoin: Int = 0
    private val exerciseViewModel by viewModels<LogExerciseViewModel> {
        LogExerciseViewModelFactory(
            LogExerciseImpl(LogExerciseDataSource())
        )
    }
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(
            UserImpl(UserDataSource())
        )
    }

    override fun initUI(view: View) {
        binding = FragmentEndOfExerciseBinding.bind(view)
        setUpData()
        setUpEvents()
        setUpScore()
    }

    private fun setUpData() {
        userId = args.userId
        coins = args.coins
        log = args.logExercise
        nextExercise = args.nextFragment
        soundCoin = setSounds.addSound(R.raw.sound_coin)
    }

    private fun setUpEvents() {
        binding.btnMainMenu.setOnClickListener {
            saveLog { returnMainMenu() }
        }
        binding.btnRepeat.setOnClickListener {
            saveLog { navigateToRepeatExercise() }
        }
        binding.btnContinue.setOnClickListener {
            saveLog { navigateToNextExercise() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { }
    }

    private fun setUpScore() {
        binding.ivAvatar.loadImageFromUrl("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/avatar_images%2Fjawi_normal.png?alt=media&token=7a732b9c-7eda-42a4-b85e-ff770809c12f")
        args.userId
        when {
            log.attemptsCorrect == log.attemptsTotal -> perfectScore()
            log.attemptsCorrect > (log.attemptsTotal / 3) -> goodScore()
            log.attemptsCorrect > 0 -> badScore()
            else -> writeScore()
        }
    }

    private fun writeScore() {
        binding.tvScore.text = String.format("${log.attemptsCorrect}/${log.attemptsTotal}")
        binding.tvCoin.text = 0.toString()
        if (coins.isGreaterThan(0)) {
            binding.tvCoin.addCoins(binding.containerCoins)
        }

    }

    private fun badScore() {
        writeScore()
        binding.ivStarFirst.setImageResource(R.drawable.ic_with_star)
        lifecycleScope.launch {
            delay(200)
            anim.startAnimation(binding.ivStarFirst, R.anim.star_scale_infinite)
        }
    }

    private fun goodScore() {
        badScore()
        binding.ivStarSecond.setImageResource(R.drawable.ic_with_star)
        lifecycleScope.launch {
            delay(500)
            anim.startAnimation(binding.ivStarSecond, R.anim.star_scale_infinite)
        }
    }

    private fun perfectScore() {
        goodScore()
        binding.btnContinue.visibility = View.VISIBLE
        binding.ivStarThird.setImageResource(R.drawable.ic_with_star)
        binding.lavConfetti.visibility = View.VISIBLE
        binding.lavConfetti.playAnimation()
        binding.ivGoldenFlash.visibility = View.VISIBLE
        anim.startAnimation(binding.ivGoldenFlash, R.anim.rotate_infinite)
        lifecycleScope.launch {
            delay(800)
            anim.startAnimation(binding.ivStarThird, R.anim.star_scale_infinite)
        }
    }

    private fun returnMainMenu() {
        val navDirection = EndOfExerciseFragmentDirections.actionEndOfExerciseToMenu(userId)
        goTo(navDirection)
    }

    private fun navigateToRepeatExercise() {
        when (log.nameExercise) {
            Exercise.NAME_KNOW_NUMBERS -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToKnowNumbers(userId)
            )
            Exercise.NAME_SELECT_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToSelectAndCount(userId)
            )
            Exercise.NAME_MOVE_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToMoveAndCount(userId)
            )
            Exercise.NAME_HOW_MANY -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToHowMany(userId)
            )
            Exercise.NAME_LESS_OR_MORE -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToLessOrMore(userId)
            )
            Exercise.NAME_ORDER_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToOrderAndCount(userId)
            )
        }
    }

    private fun navigateToNextExercise() {
        when (nextExercise) {
            Exercise.NAME_KNOW_NUMBERS -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToKnowNumbers(userId)
            )
            Exercise.NAME_SELECT_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToSelectAndCount(userId)
            )
            Exercise.NAME_MOVE_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToMoveAndCount(userId)
            )
            Exercise.NAME_HOW_MANY -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToHowMany(userId)
            )
            Exercise.NAME_LESS_OR_MORE -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToLessOrMore(userId)
            )
            Exercise.NAME_ORDER_AND_COUNT -> goTo(
                EndOfExerciseFragmentDirections.actionEndOfExerciseToOrderAndCount(userId)
            )
        }
    }

    private fun saveLog(action: () -> Unit) {
        exerciseViewModel.addLog(userId, log).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Guardando datos...")
                is Resource.Success -> {
                    updateUser(action)
                }
                is Resource.Failure -> {
                    loadingDialog.dismiss()
                    mContext.showDialogInformation(
                        icon = InformationDialog.ICON_ERROR,
                        title = "¡Ups! Ocurrio un error",
                        text = "No se pudieron guardar los datos.")
                }
            }
        })
    }

    private fun updateUser(action: () -> Unit) {
        userViewModel.addCoins(userId, coins).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.setText("Actualizando usuario...")
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    action()
                }
                is Resource.Failure -> {
                    loadingDialog.dismiss()
                    mContext.showDialogInformation(
                        icon = InformationDialog.ICON_ERROR,
                        title = "¡Ups! Ocurrio un error",
                        text = "No se pudieron guardar los datos.")
                }
            }
        })
    }

    private fun TextView.addCoins(view: View) {
        val textView: TextView = this

        lifecycleScope.launch {
            for (coin in 1..coins) {
                setSounds.playSound(soundCoin)
                textView.text = coin.toString()
                anim.startAnimation(view, R.anim.fast_zoom)
                delay(100)
            }
        }
    }
}