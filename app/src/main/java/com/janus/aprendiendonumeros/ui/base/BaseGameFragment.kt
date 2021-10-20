package com.janus.aprendiendonumeros.ui.base

import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.ui.fragment.game.*
import com.janus.aprendiendonumeros.ui.utilities.SetSound
import com.janus.aprendiendonumeros.ui.utilities.goTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseGameFragment(
    layoutId: Int,
    private val nameExercise: String,
    private val nextExercise: String,
) : BaseFragment(layoutId) {

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
    }

    protected fun playSoundCorrect() = setSounds.playSound(soundCorrect)
    protected fun playSoundIncorrect() = setSounds.playSound(soundIncorrect)
    protected fun playSoundCoin() = setSounds.playSound(soundCoin)

    protected fun returnToMainMenu() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            finish(0)
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

    private fun navigateToFragmentEndExercise(logExercise: LogExercise) {
        when (logExercise.nameExercise) {
            Exercise.NAME_KNOW_NUMBERS -> {
                val navDirection = KnowNumbersFragmentDirections
                    .actionKnowNumbersToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_SELECT_AND_COUNT -> {
                val navDirection = SelectAndCountFragmentDirections
                    .actionSelectAndCountToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_MOVE_AND_COUNT -> {
                val navDirection = MoveAndCountFragmentDirections
                    .actionMoveAndCountToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_HOW_MANY -> {
                val navDirection = HowManyFragmentDirections
                    .actionHowManyToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_LESS_OR_MORE -> {
                val navDirection = LessOrMoreFragmentDirections
                    .actionLessOrMoreToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_ORDER_AND_COUNT -> {
                val navDirection = OrderAndCountFragmentDirections
                    .actionOrderAndCountToEndOfExercise(userId, coins, nextExercise, logExercise)
                goTo(navDirection)
            }
        }
    }

    protected fun startTime() {
        lifecycleScope.launch {

        }
    }
}