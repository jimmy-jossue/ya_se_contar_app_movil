package com.janus.aprendiendonumeros.domain.exercise

import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.remote.ExerciseDataSource

class ExerciseImpl(private val dataSource: ExerciseDataSource) : ExerciseProvider {

    override suspend fun createExercises(userId: String) =
        dataSource.createExercises(userId)

    override suspend fun getExercise(userId: String, nameExercise: String): Exercise =
        dataSource.getExercise(userId, nameExercise)

    override suspend fun getAllExercises(userId: String): List<Exercise> =
        dataSource.getAllExercises(userId)

    override suspend fun setExercise(userId: String, exercise: Exercise) =
        dataSource.setExercise(userId, exercise)

    override suspend fun updateExercise(
        userId: String,
        nameExercise: String,
        data: Map<String, Any>,
    ) =
        dataSource.updateExercise(userId, nameExercise, data)
}