package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.repository.UserProfileProvider
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserProfileViewModel(private val repository: UserProfileProvider) : ViewModel() {

    fun fetchUser() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repository.getUser())
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun fetchUsers() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repository.getUsers())
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class UserProfileViewModelFactory(private val repository: UserProfileProvider): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserProfileProvider::class.java).newInstance(repository)
    }
}