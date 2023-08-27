package com.janus.aprendiendonumeros.ui.base

import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.ui.animation.UIAnimations
import com.janus.aprendiendonumeros.ui.fragment.game.*
import com.janus.aprendiendonumeros.ui.listener.Talkative
import com.janus.aprendiendonumeros.ui.utilities.SetSound
import com.janus.aprendiendonumeros.ui.utilities.Sound
import com.janus.aprendiendonumeros.ui.utilities.goTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseGameFragment(
    layoutId: Int,
    protected val nameExercise: String,
    private val nextExercise: String,
) : BaseFragment(layoutId) {

    protected val talkative: Talkative by lazy { Talkative(requireContext()) }
    protected val animator: UIAnimations by lazy { UIAnimations(mContext) }
    protected val questionAudio by lazy { Sound(mContext) }


    protected var userId: String = ""
    protected var coins: Int = 0
    private lateinit var setSounds: SetSound
    private var soundCorrect: Int = 0
    private var soundIncorrect: Int = 0
    private var soundCoin: Int = 0
    protected val logExercise: LogExercise = LogExercise()

    abstract fun initGame()

    override fun initUI(view: View) {
        setSounds = SetSound(mContext)
        soundCoin = setSounds.addSound(R.raw.sound_coin)
        soundCorrect = setSounds.addSound(R.raw.sound_correct)
        soundIncorrect = setSounds.addSound(R.raw.sound_incorrect)
        talkative.speakOut("")
    }

    protected fun playSoundCorrect() = setSounds.playSound(soundCorrect)
    protected fun playSoundIncorrect() = setSounds.playSound(soundIncorrect)
    protected fun playSoundCoin() = setSounds.playSound(soundCoin)

    protected fun returnToMainMenu() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            finish(0)
        }
    }

    protected fun sayInstruction(
        urlQuestionAudio: String,
        textQuestion: String,
        onEndAction: () -> Unit = {},
    ) {
        if (urlQuestionAudio.isNotEmpty()) {
            questionAudio.play {
                onEndAction()
            }
        } else {
            talkative.speakOut(text = textQuestion) {
                onEndAction()
            }
        }
    }

    protected fun isPerfectScore(): Boolean {
        return (logExercise.attemptsCorrect == logExercise.attemptsTotal)
    }

    protected fun correct(action: () -> Unit) {
        logExercise.attemptsCorrect++
        action()
    }

    protected fun incorrect(action: () -> Unit) {
        logExercise.attemptsIncorrect++
        action()
    }

    protected fun finish(waitTime: Long = 1500, actionBefore: () -> Unit = {}) {
        lifecycleScope.launch {
            actionBefore()
            delay(waitTime)
            navigateToFragmentEndExercise(logExercise)
        }
    }

    protected fun navigateToFragmentEndExercise(logExercise: LogExercise) {
        when (logExercise.nameExercise) {
            Exercise.NAME_KNOW_NUMBERS -> {
                val navDirection = KnowNumbersFragmentDirections
                    .actionKnowNumbersToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
            Exercise.NAME_SELECT_AND_COUNT -> {
                val navDirection = SelectAndCountFragmentDirections
                    .actionSelectAndCountToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
            Exercise.NAME_MOVE_AND_COUNT -> {
                val navDirection = MoveAndCountFragmentDirections
                    .actionMoveAndCountToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
            Exercise.NAME_HOW_MANY -> {
                val navDirection = HowManyFragmentDirections
                    .actionHowManyToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
            Exercise.NAME_LESS_OR_MORE -> {
                val navDirection = LessOrMoreFragmentDirections
                    .actionLessOrMoreToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
            Exercise.NAME_ORDER_AND_COUNT -> {
                val navDirection = OrderAndCountFragmentDirections
                    .actionOrderAndCountToEndOfExercise(logExercise = logExercise, userId = userId, coins = coins, nextFragment = nextExercise)
                goTo(navDirection)
            }
        }
    }

    protected fun startTime() {
        lifecycleScope.launch {

        }
    }
}