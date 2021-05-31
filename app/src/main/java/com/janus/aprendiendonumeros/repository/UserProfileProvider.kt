package com.janus.aprendiendonumeros.repository

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User

interface UserProfileProvider {
    suspend fun getUser(): Resource<User>
    suspend fun getUsers(): Resource<List<User>>
}