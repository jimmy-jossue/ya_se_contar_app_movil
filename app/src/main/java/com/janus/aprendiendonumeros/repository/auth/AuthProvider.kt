package com.janus.aprendiendonumeros.repository.auth

import com.janus.aprendiendonumeros.data.model.User

interface AuthProvider {
    suspend fun signInAdult(nickName: String, passwordAdult: String): String
    suspend fun signInChild(passwordChild: String): String
    suspend fun signUp(userId: String, user: User)
    suspend fun fieldExistsInUser(field: String, value: Any): Boolean
    suspend fun getUser(userId: String): User
}