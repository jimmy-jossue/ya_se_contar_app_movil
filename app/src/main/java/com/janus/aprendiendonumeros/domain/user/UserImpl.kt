package com.janus.aprendiendonumeros.domain.user

import com.janus.aprendiendonumeros.data.remote.UserDataSource

class UserImpl(private val dataSource: UserDataSource) : UserProvider {
    override suspend fun addCoins(userId: String, coins: Int) = dataSource.addCoins(userId, coins)
}