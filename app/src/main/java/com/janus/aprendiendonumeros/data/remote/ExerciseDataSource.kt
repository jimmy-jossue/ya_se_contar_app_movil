package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class ExerciseDataSource {

    suspend fun getExercise(nameExercise: String): Exercise {
        return Exercise()
    }

    suspend fun setExercise(userId: String, exercise: Exercise) {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
            .document(userId)
            .collection(Exercise.PATH_EXERCISES)

        val data = hashMapOf(
            Exercise.NAME to exercise.name,
            Exercise.IMAGE to exercise.image,
            Exercise.POSITION to exercise.position,
            Exercise.STATUS to exercise.status
        )

        usersCollectionReference.document(exercise.name).set(data).await()
    }
}