package com.janus.aprendiendonumeros.ui.animation.coins

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.core.animation.doOnEnd
import kotlin.random.Random

class AnimationReward(
    private val containerCoins: ConstraintLayout,
    private var coinPositionToX: Float = 0f,
    private var coinPositionToY: Float = 0f,
    private var coinWidth: Int = WRAP_CONTENT,
    private var coinHeight: Int = WRAP_CONTENT,
    var onEndAnimationCoin: () -> Unit = {},
    var onEndAnimationAllCoins: () -> Unit = {},
) : Runnable {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var rewardAmount: Int = 0
    private var coinCount = 0
    private var coinPositionFromX: Float = 0f
    private var coinPositionFromY: Float = 0f
    private var isLastCoin: Boolean = false

    companion object {
        const val REWARD_SIMPLE: Int = 10
        const val REWARD_MAXIMUM: Int = 100
        private const val ALPHA: String = "alpha"
        private const val ROTATION: String = "rotation"
        private const val TRANSLATION_X: String = "translationX"
        private const val TRANSLATION_Y: String = "translationY"
        private const val SCALE_X: String = "scaleX"
        private const val SCALE_Y: String = "scaleY"
    }

    fun give(
        rewardAmount: Int,
        coinPositionFromX: Float,
        coinPositionFromY: Float,
    ) {
        this.rewardAmount = rewardAmount
        this.coinPositionFromX = coinPositionFromX
        this.coinPositionFromY = coinPositionFromY
        this.coinCount = 0
        handler.removeCallbacks(this)
        handler.post(this)
    }

    fun setPositionDestinationCoins(toX: Float, toY: Float) {
        coinPositionToX = toX
        coinPositionToY = toY
    }

    fun setSizeCoins(width: Int, height: Int) {
        coinWidth = width
        coinHeight = height
    }

    private fun randomArcAnimation(
        view: AppCompatImageView,
        translateDurationRange: Pair<Long, Long> = 500L to 1_200L,
        rotationDurationRange: Pair<Long, Long> = 200L to 400L,
    ) {
        val translateDuration =
            Random.nextLong(translateDurationRange.first, translateDurationRange.second)
        AnimatorSet().apply {
            val anim = this
            this.playTogether(
                ObjectAnimator.ofFloat(view, ROTATION, 0f, 360f).apply {
                    duration =
                        Random.nextLong(rotationDurationRange.first, rotationDurationRange.second)
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = LinearInterpolator()
                },
                ObjectAnimator.ofFloat(view, ALPHA, 0f).apply {
                    duration = translateDuration / 4
                    startDelay = (translateDuration / 4) * 3
                    interpolator = AccelerateInterpolator(Random.nextDouble(.5, 1.5).toFloat())
                },
                ObjectAnimator.ofFloat(view, TRANSLATION_X, coinPositionFromX, coinPositionToX)
                    .apply {
                        duration = translateDuration
                        interpolator = AccelerateInterpolator(Random.nextDouble(.5, 1.5).toFloat())
                    },
                ObjectAnimator.ofFloat(view, TRANSLATION_Y, coinPositionFromY, coinPositionToY)
                    .apply {
                        duration = translateDuration
                        interpolator = LinearInterpolator()
                        doOnEnd { anim.end() }
                    },
                ObjectAnimator.ofFloat(view, SCALE_X, 1.1f, 1.0f).apply {
                    duration = translateDuration / 3
                    interpolator = LinearInterpolator()
                },
                ObjectAnimator.ofFloat(view, SCALE_Y, 1.1f, 1.0f).apply {
                    duration = translateDuration / 3
                    interpolator = LinearInterpolator()
                }
            )
            doOnEnd {
                containerCoins.removeView(view)
                onEndAnimationCoin()
            }
        }.start()
    }

    override fun run() {
        if (coinCount < rewardAmount) {
            val coin = Coin.addTo(containerCoins, coinWidth, coinHeight)
            randomArcAnimation(coin)
            handler.postDelayed(this, 50)
            coinCount++
        } else {
            handler.removeCallbacks(this)
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(this)
    }
}

/*
ObjectAnimator.ofFloat(view, ALPHA, 0f).apply {
    duration = translateDuration/4
    startDelay = (translateDuration/4)*3
    interpolator = AccelerateInterpolator(Random.nextDouble(.5, 1.5).toFloat())
},
ObjectAnimator.ofFloat(view, TRANSLATION_X, coinPositionFromX, coinPositionToX).apply{
    duration = translateDuration
    interpolator = AccelerateInterpolator(Random.nextDouble(.5, 1.5).toFloat())
},
*/