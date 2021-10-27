package com.janus.aprendiendonumeros.data.model

data class Game(
    val instructionAudio: String = "",
    val figure: Figure,
    val number: Int = 0,
    val firstDistraction: Int = 0,
    val secondDistraction: Int = 0,
)


