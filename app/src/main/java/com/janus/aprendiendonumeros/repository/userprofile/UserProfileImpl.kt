package com.janus.aprendiendonumeros.repository.userprofile

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.UserProfileDataSource

class UserProfileImpl(private val dataSource: UserProfileDataSource) : UserProfileProvider {
    override suspend fun getUser(): Resource<User> = dataSource.getUser()
    override suspend fun getUser(userId: String): Resource<User> = dataSource.getUser()
    override suspend fun getUsers(): Resource<List<User>> = dataSource.getUsers()
}