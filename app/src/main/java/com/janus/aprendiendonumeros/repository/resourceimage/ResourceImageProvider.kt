package com.janus.aprendiendonumeros.repository.resourceimage

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ResourceImageDataSource

interface ResourceImageProvider {
    suspend fun getResourceImages(level: ResourceImageDataSource.Level): Resource<List<ResourceImage>>
}