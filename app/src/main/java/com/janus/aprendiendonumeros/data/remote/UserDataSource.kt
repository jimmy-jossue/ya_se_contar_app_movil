package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDataSource {

    companion object {
        const val PATH: String = "/users"
        const val NICK_NAME = "nickName"
        const val GENDER = "gender"
        const val IMAGE = "image"
        const val PASSWORD_CHILD = "passwordChild"
        const val PASSWORD_ADULT = "passwordAdult"
        const val BIRTH_DATE = "birthDate"
        const val EMAIL = "email"
        const val COINS = "coins"
    }

    suspend fun updateUser(userId: String, data: Map<String, Any>) {
        val usersDocumentReference =
            FirebaseFirestore
                .getInstance()
                .collection(PATH)
                .document(userId)

        usersDocumentReference.update(data).await()
    }

    private suspend fun getCurrentCoins(userId: String): Int {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(PATH)

        val userDocument = usersCollectionReference.document(userId).get().await()
        return userDocument.data?.get(COINS).toString().toInt()
    }

    suspend fun addCoins(userId: String, coins: Int) {
        val currentCoins: Int = getCurrentCoins(userId)
        val newCoins: Int = currentCoins + coins
        val usersDocumentReference =
            FirebaseFirestore
                .getInstance()
                .collection(PATH)
                .document(userId)

        val data: Map<String, Any> = hashMapOf(
            COINS to newCoins
        )
        usersDocumentReference.update(data).await()
    }
}