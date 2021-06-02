package com.janus.aprendiendonumeros.repository

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.UserProfileDataSource

class UserProfileImpl(private val dataSource: UserProfileDataSource): UserProfileProvider{
    override suspend fun getUser(): Resource<User> = dataSource.getUser()
    override suspend fun getUsers(): Resource<List<User>> = dataSource.getUsers()
}