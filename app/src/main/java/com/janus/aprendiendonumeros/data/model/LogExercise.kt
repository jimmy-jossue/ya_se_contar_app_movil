package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.ui.utilities.Numbers
import java.io.Serializable

data class LogExercise(
    var nameExercise: String = "",
    var level: String = "first",
    var attemptsCorrect: Int = 0,
    var attemptsIncorrect: Int = 0,
    var attemptsTotal: Int = 0,
    var date: Timestamp = Timestamp(Numbers.getCurrentDate("America/Mexico_City")),
    var timeElapsedInSeconds: Int = 0,
) : Serializable {
    companion object {
        const val PATH_LOG_EXERCISES: String = "logs_exercises"
        const val NAME_EXERCISE: String = "nameExercise"
        const val LEVEL: String = "level"
        const val ATTEMPTS_CORRECT: String = "attemptsCorrect"
        const val ATTEMPTS_INCORRECT: String = "attemptsIncorrect"
        const val ATTEMPTS_TOTAL: String = "attemptsTotal"
        const val DATE: String = "date"
        const val TIME_ELAPSED_IN_SECONDS: String = "timeElapsedInMilliseconds"
    }
}