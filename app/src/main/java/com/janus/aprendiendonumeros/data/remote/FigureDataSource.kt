package com.janus.aprendiendonumeros.data.remote

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.janus.aprendiendonumeros.data.model.Figure
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class FigureDataSource {

    enum class Level { FIRST, SECOND, THIRD }

    companion object {
        private const val FIRST_LEVEL: String = "/level_first_resource_images"
        private const val SECOND_LEVEL: String = "/level_second_resource_images"
        private const val THIRD_LEVEL: String = "/level_third_resource_images"
        private const val PATH_USER_PROFILE_IMAGES: String = "/user_profile_images"
        private const val QUALITY_HIGH: Int = 100
    }

    suspend fun getAll(level: String): List<Figure> {
        val list = mutableListOf<Figure>()
        val path: String = when (level) {
            Level.FIRST.toString() -> FIRST_LEVEL
            Level.SECOND.toString() -> SECOND_LEVEL
            else -> THIRD_LEVEL
        }

        val documentSnapshot = FirebaseFirestore.getInstance().collection(path).get().await()
        for (document in documentSnapshot.documents) {
            document.toObject(Figure::class.java)?.let {
                list.add(it)
            }
        }
        return list
    }

    suspend fun saveProfileImage(imageBitmap: Bitmap, idUser: String): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val userProfileImageRef = storageRef.child("$PATH_USER_PROFILE_IMAGES/$idUser")

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(
            Bitmap.CompressFormat.PNG,
            QUALITY_HIGH,
            baos
        )
        val data = baos.toByteArray()
        val uploadTask = userProfileImageRef.putBytes(data).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
}