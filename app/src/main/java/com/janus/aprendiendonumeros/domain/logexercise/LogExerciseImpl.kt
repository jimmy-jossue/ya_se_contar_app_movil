package com.janus.aprendiendonumeros.domain.logexercise

import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.remote.LogExerciseDataSource

class LogExerciseImpl(private val dataSource: LogExerciseDataSource) : LogExerciseProvider {
    override suspend fun addLog(userId: String, log: LogExercise) = dataSource.addLog(userId, log)
}