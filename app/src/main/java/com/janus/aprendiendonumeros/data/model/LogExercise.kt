package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.data.remote.FigureDataSource
import com.janus.aprendiendonumeros.ui.utilities.Numbers
import java.io.Serializable

data class LogExercise(
    var nameExercise: String = "",
    var level: String = FigureDataSource.Level.FIRST.toString(),
    var attemptsCorrect: Int = 0,
    var attemptsIncorrect: Int = 0,
    var attemptsTotal: Int = 0,
    var date: Timestamp = Timestamp(Numbers.getCurrentDate("America/Mexico_City")),
    var timeElapsedInSeconds: Int = 0,
) : Serializable