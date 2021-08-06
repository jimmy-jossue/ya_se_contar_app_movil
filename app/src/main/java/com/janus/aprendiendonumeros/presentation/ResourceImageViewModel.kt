package com.janus.aprendiendonumeros.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.remote.ResourceImageDataSource
import com.janus.aprendiendonumeros.repository.resourceimage.ResourceImageProvider
import kotlinx.coroutines.Dispatchers

class ResourceImageViewModel(private val repository: ResourceImageProvider) : ViewModel() {

    fun fetchResourceImage(level: ResourceImageDataSource.Level) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repository.getResourceImages(level))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }
}

class ResourceImageViewModelFactory(private val repository: ResourceImageProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ResourceImageProvider::class.java).newInstance(repository)
    }
}