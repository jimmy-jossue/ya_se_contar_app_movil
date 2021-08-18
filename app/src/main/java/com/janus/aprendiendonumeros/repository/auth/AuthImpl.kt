package com.janus.aprendiendonumeros.repository.auth

import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.AuthDataSource

class AuthImpl(private val dataSource: AuthDataSource) : AuthProvider {
    override suspend fun signInAdult(nickName: String, passwordAdult: String): String =
        dataSource.signInAdult(nickName, passwordAdult)

    override suspend fun signInChild(passwordChild: String): String =
        dataSource.signInChild(passwordChild)

    override suspend fun signUp(userId: String, user: User) =
        dataSource.signUp(userId, user)

    override suspend fun fieldExistsInUser(field: String, value: Any): Boolean =
        dataSource.fieldExistsInUser(field, value)

    override suspend fun getUser(userId: String): User =
        dataSource.getUser(userId)
}