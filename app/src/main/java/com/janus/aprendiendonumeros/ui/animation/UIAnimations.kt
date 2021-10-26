package com.janus.aprendiendonumeros.ui.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.core.animation.doOnEnd
import kotlin.random.Random

class UIAnimations(private val context: Context) {

    companion object {
        private const val ALPHA: String = "alpha"
        private const val ROTATION: String = "rotation"
        private const val TRANSLATION_X: String = "translationX"
        private const val TRANSLATION_Y: String = "translationY"
        private const val SCALE_X: String = "scaleX"
        private const val SCALE_Y: String = "scaleY"
    }

    fun startAnimation(view: View, @AnimRes idAnimation: Int) {
        val anim: Animation =
            AnimationUtils.loadAnimation(context, idAnimation)
        view.startAnimation(anim)
    }

    fun fadeInWithScale(view: View, durationMs: Long = 100, onEndAction: () -> Unit = {}) {
        val acceleratorFactor = Random.nextDouble(.5, 1.5).toFloat()
        AnimatorSet().apply {
            this.playTogether(
                ObjectAnimator.ofFloat(view, ALPHA, 1f).apply {
                    duration = durationMs / 4
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                },
                ObjectAnimator.ofFloat(view, SCALE_X, 0.35f, 1.0f).apply {
                    duration = durationMs
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                },
                ObjectAnimator.ofFloat(view, SCALE_Y, 0.35f, 1.0f).apply {
                    duration = durationMs
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                }
            )
            doOnEnd { onEndAction() }
        }.start()
    }

    fun fadeOutWithScale(view: View, durationMs: Long = 100, onEndAction: () -> Unit = {}) {
        val acceleratorFactor = Random.nextDouble(.5, 1.5).toFloat()
        AnimatorSet().apply {
            this.playTogether(
                ObjectAnimator.ofFloat(view, ALPHA, 1f).apply {
                    duration = durationMs / 4
                    startDelay = (durationMs / 4) * 3
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                },
                ObjectAnimator.ofFloat(view, SCALE_X, 1f, 0.2f).apply {
                    duration = durationMs
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                },
                ObjectAnimator.ofFloat(view, SCALE_Y, 1f, 0.2f).apply {
                    duration = durationMs
                    interpolator = AccelerateInterpolator(acceleratorFactor)
                }
            )
            doOnEnd { onEndAction() }
        }.start()
    }

}