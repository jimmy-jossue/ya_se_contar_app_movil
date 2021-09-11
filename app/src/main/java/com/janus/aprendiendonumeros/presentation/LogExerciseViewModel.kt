package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.domain.logexercise.LogExerciseProvider
import kotlinx.coroutines.Dispatchers

class LogExerciseViewModel(private val repository: LogExerciseProvider) : ViewModel() {

    fun addLog(idUser: String, log: LogExercise) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.addLog(idUser, log)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class LogExerciseViewModelFactory(private val repository: LogExerciseProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LogExerciseProvider::class.java).newInstance(repository)
    }
}