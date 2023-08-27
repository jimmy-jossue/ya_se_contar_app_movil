package com.janus.aprendiendonumeros.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.domain.figure.FigureProvider
import kotlinx.coroutines.Dispatchers

class ImageViewModel(private val repository: FigureProvider) : ViewModel() {

    fun getImages(level: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.getAll(level)))
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

class ImageViewModelFactory(private val repository: FigureProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FigureProvider::class.java).newInstance(repository)
    }
}