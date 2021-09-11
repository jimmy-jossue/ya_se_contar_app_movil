package com.janus.aprendiendonumeros.domain.user

interface UserProvider {
    suspend fun addCoins(userId: String, coins: Int)
}