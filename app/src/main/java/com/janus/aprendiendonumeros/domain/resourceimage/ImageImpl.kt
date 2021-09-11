package com.janus.aprendiendonumeros.domain.resourceimage

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource

class ImageImpl(private val dataSource: ImageDataSource) : ImageProvider {
    override suspend fun getImages(level: ImageDataSource.Level): List<ResourceImage> =
        dataSource.getImages(level)

    override suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String =
        dataSource.saveProfileImage(imageBitmap, idUser)
}