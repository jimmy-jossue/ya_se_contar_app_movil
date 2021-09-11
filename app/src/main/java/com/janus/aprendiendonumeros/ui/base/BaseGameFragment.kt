package com.janus.aprendiendonumeros.ui.base

import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.ui.fragment.game.*
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.Numbers
import com.janus.aprendiendonumeros.ui.utilities.SetSound
import com.janus.aprendiendonumeros.ui.utilities.goTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseGameFragment(
    layoutId: Int,
    private val nameExercise: String,
    private val nextExercise: String,
) :
    BaseFragment(layoutId) {

    protected var countSeconds: Int = 0
    private var attemptsCorrect: Int = 0
    private var attemptsIncorrect: Int = 0
    protected var attemptsTotal: Int = 0
    protected lateinit var level: ImageDataSource.Level
    protected var userId: String = ""
    protected var coins: Int = 0
    private lateinit var setSounds: SetSound
    private var soundCorrect: Int = 0
    private var soundIncorrect: Int = 0
    private var soundCoin: Int = 0
    private val logExercise: LogExercise = LogExercise()

    abstract fun initGame()

    override fun initUI(view: View) {
        setSounds = SetSound(mContext)
        soundCoin = setSounds.addSound(R.raw.sound_coin)
        soundIncorrect = setSounds.addSound(R.raw.sound_incorrect)
    }

    protected fun playSoundCorrect() = setSounds.playSound(soundIncorrect)
    protected fun playSoundIncorrect() = setSounds.playSound(soundIncorrect)
    protected fun playSoundCoin() = setSounds.playSound(soundCoin)

    protected fun returnToMainMenu() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            finish(0) {}
        }
    }

    protected fun isPerfectScore(): Boolean {
        return (attemptsCorrect == attemptsTotal)
    }

    protected fun correct(action: () -> Unit) {
        attemptsCorrect++
        action()
    }

    protected fun incorrect(action: () -> Unit) {
        attemptsIncorrect++
        action()
    }

    protected fun finish(waitTime: Long = 1500, actionBefore: () -> Unit) {
        lifecycleScope.launch {
            actionBefore()
            delay(waitTime)
            val logExercise = LogExercise(
                nameExercise = nameExercise,
                level = level.toString(),
                attemptsCorrect = attemptsCorrect,
                attemptsIncorrect = attemptsIncorrect,
                attemptsTotal = attemptsTotal,
                date = Timestamp(Numbers.getCurrentDate(Constant.TIME_ZONE)),
                timeElapsedInSeconds = countSeconds
            )
            navigateToFragmentEndExercise(logExercise)
        }
    }

    private fun navigateToFragmentEndExercise(logExercise: LogExercise) {
        when (nameExercise) {
            Exercise.NAME_KNOW_NUMBERS -> {
                val navDirection =
                    KnowNumbersFragmentDirections.actionKnowNumbersToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_SELECT_AND_COUNT -> {
                val navDirection =
                    SelectAndCountFragmentDirections.actionSelectAndCountToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_MOVE_AND_COUNT -> {
                val navDirection =
                    MoveAndCountFragmentDirections.actionMoveAndCountToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_HOW_MANY -> {
                val navDirection =
                    HowManyFragmentDirections.actionHowManyToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_LESS_OR_MORE -> {
                val navDirection =
                    LessOrMoreFragmentDirections.actionLessOrMoreToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
            Exercise.NAME_ORDER_AND_COUNT -> {
                val navDirection =
                    OrderAndCountFragmentDirections.actionOrderAndCountToEndOfExercise(userId,
                        coins,
                        nextExercise,
                        logExercise)
                goTo(navDirection)
            }
        }
    }

    protected fun startTime() {
        lifecycleScope.launch {

        }
    }
}