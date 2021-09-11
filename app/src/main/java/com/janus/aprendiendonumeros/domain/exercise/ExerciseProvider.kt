package com.janus.aprendiendonumeros.domain.exercise

import com.janus.aprendiendonumeros.data.model.Exercise

interface ExerciseProvider {
    suspend fun createExercises(userId: String)
    suspend fun getExercise(userId: String, nameExercise: String): Exercise
    suspend fun getAllExercises(userId: String): List<Exercise>
    suspend fun setExercise(userId: String, exercise: Exercise)
    suspend fun updateExercise(userId: String, nameExercise: String, data: Map<String, Any>)
}