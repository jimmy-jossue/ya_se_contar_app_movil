package com.janus.aprendiendonumeros.repository.resourceimage

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource

class ResourceImageImpl(private val dataSource: ImageDataSource) : ResourceImageProvider {
    override suspend fun getResourceImages(level: ImageDataSource.Level): List<ResourceImage> =
        dataSource.getResourceImages(level)

    override suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String =
        dataSource.saveProfileImage(imageBitmap, idUser)
}