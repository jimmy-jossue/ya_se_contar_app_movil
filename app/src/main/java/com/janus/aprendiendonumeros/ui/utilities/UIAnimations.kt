package com.janus.aprendiendonumeros.ui.utilities

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class UIAnimations(private val context: Context) {

    fun startSimple(view: View, idAnimation: Int) {
        val anim: Animation =
            AnimationUtils.loadAnimation(context, idAnimation)
        view.startAnimation(anim)
    }
}