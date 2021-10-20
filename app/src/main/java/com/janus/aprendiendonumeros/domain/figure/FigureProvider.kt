package com.janus.aprendiendonumeros.domain.figure

import android.graphics.Bitmap
import com.janus.aprendiendonumeros.data.model.Figure

interface FigureProvider {
    suspend fun getAll(level: String): List<Figure>
    suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String
}