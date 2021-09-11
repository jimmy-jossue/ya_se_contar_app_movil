package com.janus.aprendiendonumeros.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.domain.resourceimage.ImageProvider
import kotlinx.coroutines.Dispatchers

class ImageViewModel(private val repository: ImageProvider) : ViewModel() {

    fun getImages(level: ImageDataSource.Level) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getImages(level)))
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

class ImageViewModelFactory(private val repository: ImageProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ImageProvider::class.java).newInstance(repository)
    }
}