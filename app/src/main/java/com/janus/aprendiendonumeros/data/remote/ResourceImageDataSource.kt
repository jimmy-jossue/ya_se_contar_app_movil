package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage
import kotlinx.coroutines.tasks.await

class ResourceImageDataSource {

    private val path: String = "level_first_resource_images"

    suspend fun getResourceImages(): Resource<List<ResourceImage>> {
        val list = mutableListOf<ResourceImage>()
        val documentSnapshot = FirebaseFirestore.getInstance().collection(path).get().await()
        for (document in documentSnapshot.documents) {
            document.toObject(ResourceImage::class.java)?.let {
                list.add(it)
            }
        }
        return Resource.Success(list)
    }
}