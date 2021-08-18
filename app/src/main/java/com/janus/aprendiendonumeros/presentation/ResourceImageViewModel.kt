package com.janus.aprendiendonumeros.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.repository.resourceimage.ResourceImageProvider
import kotlinx.coroutines.Dispatchers

class ResourceImageViewModel(private val repository: ResourceImageProvider) : ViewModel() {

    fun fetchResourceImage(level: ImageDataSource.Level) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getResourceImages(level)))
        } catch (ex: Exception) {
            emit(Resource.Failure(ex))
        }
    }

    fun saveProfileImage(imageBitmap: Bitmap, idUser: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.saveProfileImage(imageBitmap, idUser)))
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