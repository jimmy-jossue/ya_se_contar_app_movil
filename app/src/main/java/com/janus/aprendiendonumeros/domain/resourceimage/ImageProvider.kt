package com.janus.aprendiendonumeros.domain.resourceimage

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource

interface ImageProvider {
    suspend fun getImages(level: ImageDataSource.Level): List<ResourceImage>
    suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String
}