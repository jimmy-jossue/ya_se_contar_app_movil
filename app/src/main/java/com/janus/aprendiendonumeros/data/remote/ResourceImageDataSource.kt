package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage
import kotlinx.coroutines.tasks.await

class ResourceImageDataSource {

    enum class Level { FIRST, SECOND, THIRD }

    private val firstLevel: String = "level_first_resource_images"
    private val secondLevel: String = "level_second_resource_images"
    private val thirdLevel: String = "level_third_resource_images"

    suspend fun getResourceImages(level: Level): Resource<List<ResourceImage>> {
        val list = mutableListOf<ResourceImage>()
        val path: String = when (level) {
            Level.FIRST -> firstLevel
            Level.SECOND -> secondLevel
            Level.THIRD -> thirdLevel
        }

        val documentSnapshot = FirebaseFirestore.getInstance().collection(path).get().await()
        for (document in documentSnapshot.documents) {
            document.toObject(ResourceImage::class.java)?.let {
                list.add(it)
            }
        }
        return Resource.Success(list)
    }
}