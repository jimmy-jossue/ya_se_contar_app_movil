package com.janus.aprendiendonumeros.domain.figure

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.Figure
import com.janus.aprendiendonumeros.data.remote.FigureDataSource

class FigureImpl(private val dataSource: FigureDataSource) : FigureProvider {
    override suspend fun getAll(level: String): List<Figure> =
        dataSource.getAll(level)

    override suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String =
        dataSource.saveProfileImage(imageBitmap, idUser)
}