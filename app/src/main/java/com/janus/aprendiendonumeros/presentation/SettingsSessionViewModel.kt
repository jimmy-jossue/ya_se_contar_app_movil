package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.repository.settingssession.SettingsSessionProvider
import kotlinx.coroutines.Dispatchers

class SettingsSessionViewModel(private val repository: SettingsSessionProvider) : ViewModel() {
    fun fetchSettingsSession() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getSettingsSession()))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class SettingsSessionViewModelFactory(private val repository: SettingsSessionProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SettingsSessionProvider::class.java)
            .newInstance(repository)
    }
}