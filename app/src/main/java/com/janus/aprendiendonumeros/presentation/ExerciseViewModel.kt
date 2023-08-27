package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.domain.exercise.ExerciseProvider
import kotlinx.coroutines.Dispatchers

class ExerciseViewModel(private val repository: ExerciseProvider) : ViewModel() {

    fun createExercises(userId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.createExercises(userId)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun getExercise(userId: String, nameExercise: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getExercise(userId, nameExercise)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun getAllExercises(userId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getAllExercises(userId)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun setExercise(userId: String, exercise: Exercise) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.setExercise(userId, exercise)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun updateExercise(
        userId: String,
        nameExercise: String,
        data: Map<String, Any>,
    ) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(repository.updateExercise(userId, nameExercise, data))
            )
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}


class ExerciseViewModelFactory(private val repository: ExerciseProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ExerciseProvider::class.java)
            .newInstance(repository)
    }
}