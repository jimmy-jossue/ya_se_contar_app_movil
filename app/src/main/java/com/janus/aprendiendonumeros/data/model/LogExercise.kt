package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.ui.utilities.Numbers

data class LogExercise(
    var nameExercise: String = "",
    var attemptsCorrect: Int = 0,
    var attemptsIncorrect: Int = 0,
    var attemptsTotal: Int = 0,
    var date: Timestamp = Timestamp(Numbers.getCurrentDate("America/Mexico_City")),
    var timeElapsedInMilliseconds: Int = 0,
) {
    companion object {
        const val NAME_EXERCISE: String = "nameExercise"
        const val ATTEMPTS_CORRECT: String = "attemptsCorrect"
        const val ATTEMPTS_INCORRECT: String = "attemptsIncorrect"
        const val ATTEMPTS_TOTAL: String = "attemptsTotal"
        const val DATE: String = "date"
        const val TIME_ELAPSED_IN_MILLISECONDS: String = "timeElapsedInMilliseconds"
    }
}