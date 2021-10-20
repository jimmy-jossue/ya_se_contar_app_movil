package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.LogExercise
import kotlinx.coroutines.tasks.await

class LogExerciseDataSource {

    companion object {
        const val PATH: String = "logs_exercises"
        const val NAME_EXERCISE: String = "nameExercise"
        const val LEVEL: String = "level"
        const val ATTEMPTS_CORRECT: String = "attemptsCorrect"
        const val ATTEMPTS_INCORRECT: String = "attemptsIncorrect"
        const val ATTEMPTS_TOTAL: String = "attemptsTotal"
        const val DATE: String = "date"
        const val TIME_ELAPSED_IN_SECONDS: String = "timeElapsedInMilliseconds"
    }

    suspend fun add(userId: String, log: LogExercise) {
        val logExercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)
            .collection(PATH)

        val data = hashMapOf(
            NAME_EXERCISE to log.nameExercise,
            LEVEL to log.level,
            ATTEMPTS_CORRECT to log.attemptsCorrect,
            ATTEMPTS_INCORRECT to log.attemptsIncorrect,
            ATTEMPTS_TOTAL to log.attemptsTotal,
            TIME_ELAPSED_IN_SECONDS to log.timeElapsedInSeconds,
            DATE to log.date
        )

        logExercisesCollectionReference
            .document()
            .set(data)
            .await()
    }

    suspend fun getAll(userId: String, nameExercise: String): List<LogExercise> {
        val list = mutableListOf<LogExercise>()

        val logExercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)
            .document(userId)
            .collection(PATH)

        val queryResult = logExercisesCollectionReference
            .whereEqualTo(NAME_EXERCISE, nameExercise)
            .get().await()

        for (document in queryResult.documents) {

            val logExercise = LogExercise(
                nameExercise = document.data?.get(NAME_EXERCISE).toString(),
                level = document.data?.get(LEVEL).toString(),
                attemptsCorrect = document.data?.get(ATTEMPTS_CORRECT) as Int,
                attemptsIncorrect = document.data?.get(ATTEMPTS_INCORRECT) as Int,
                attemptsTotal = document.data?.get(ATTEMPTS_TOTAL) as Int,
                date = document.data?.get(DATE) as Timestamp,
                timeElapsedInSeconds = document.data?.get(ATTEMPTS_TOTAL) as Int
            )
            list.add(logExercise)
        }
        return list
    }
}