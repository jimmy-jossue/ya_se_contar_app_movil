package com.janus.aprendiendonumeros.ui.base

import androidx.appcompat.widget.AppCompatButton

interface Questionable {
    fun setUpQuestion()
    fun generateQuestion()
    fun generateOptions()
    fun evaluateOption(button: AppCompatButton)
}