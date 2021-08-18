package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.repository.auth.AuthProvider
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val repository: AuthProvider) : ViewModel() {

    fun signInAdult(nickName: String, passwordAdult: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.signInAdult(nickName, passwordAdult)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun signInChild(passwordChild: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.signInChild(passwordChild)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun signUp(userId: String, user: User) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.signUp(userId, user)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun fieldExistsInUser(field: String, value: Any) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.fieldExistsInUser(field, value)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun getUser(userId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getUser(userId)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class AuthViewModelFactory(private val repository: AuthProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthProvider::class.java)
            .newInstance(repository)
    }
}