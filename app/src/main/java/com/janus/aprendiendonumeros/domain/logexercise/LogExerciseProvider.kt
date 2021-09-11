package com.janus.aprendiendonumeros.domain.logexercise

import com.janus.aprendiendonumeros.data.model.LogExercise

interface LogExerciseProvider {
    suspend fun addLog(userId: String, log: LogExercise)
}