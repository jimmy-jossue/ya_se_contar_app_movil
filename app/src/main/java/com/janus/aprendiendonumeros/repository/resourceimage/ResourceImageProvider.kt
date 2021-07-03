package com.janus.aprendiendonumeros.repository.resourceimage

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage

interface ResourceImageProvider {
    suspend fun getResourceImages(): Resource<List<ResourceImage>>
}