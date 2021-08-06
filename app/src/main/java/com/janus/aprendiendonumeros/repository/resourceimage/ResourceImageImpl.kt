package com.janus.aprendiendonumeros.repository.resourceimage

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ResourceImageDataSource

class ResourceImageImpl(private val dataSource: ResourceImageDataSource) : ResourceImageProvider {
    override suspend fun getResourceImages(level: ResourceImageDataSource.Level): Resource<List<ResourceImage>> =
        dataSource.getResourceImages(level)
}