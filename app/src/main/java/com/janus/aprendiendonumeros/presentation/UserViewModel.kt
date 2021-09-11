package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.domain.user.UserProvider
import kotlinx.coroutines.Dispatchers

class UserViewModel(private val repository: UserProvider) : ViewModel() {

    fun addCoins(userId: String, coins: Int) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.addCoins(userId, coins)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class UserViewModelFactory(private val repository: UserProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserProvider::class.java)
            .newInstance(repository)
    }
}