package com.janus.aprendiendonumeros.repository.resourceimage

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource

interface ResourceImageProvider {
    suspend fun getResourceImages(level: ImageDataSource.Level): List<ResourceImage>
    suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String
}