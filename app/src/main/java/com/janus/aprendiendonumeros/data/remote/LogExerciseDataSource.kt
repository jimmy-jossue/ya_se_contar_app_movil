package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class LogExerciseDataSource {

    suspend fun addLog(userId: String, log: LogExercise) {
        val exercisesCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)
            .collection(LogExercise.PATH_LOG_EXERCISES)

        val data = hashMapOf(
            LogExercise.NAME_EXERCISE to log.nameExercise,
            LogExercise.LEVEL to log.level,
            LogExercise.ATTEMPTS_CORRECT to log.attemptsCorrect,
            LogExercise.ATTEMPTS_INCORRECT to log.attemptsIncorrect,
            LogExercise.ATTEMPTS_TOTAL to log.attemptsTotal,
            LogExercise.TIME_ELAPSED_IN_SECONDS to log.timeElapsedInSeconds,
            LogExercise.DATE to log.date
        )

        exercisesCollectionReference
            .document()
            .set(data)
            .await()
    }
}