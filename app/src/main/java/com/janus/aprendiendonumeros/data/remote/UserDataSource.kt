package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class UserDataSource {
    suspend fun updateUser(userId: String, data: Map<String, Any>) {
        val usersDocumentReference =
            FirebaseFirestore
                .getInstance()
                .collection(User.PATH_USERS)
                .document(userId)

        usersDocumentReference.update(data).await()
    }

    private suspend fun getCurrentCoins(userId: String): Int {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)

        val userDocument = usersCollectionReference.document(userId).get().await()
        return userDocument.data?.get(User.COINS).toString().toInt()
    }

    suspend fun addCoins(userId: String, coins: Int) {
        val currentCoins: Int = getCurrentCoins(userId)
        val newCoins: Int = currentCoins + coins
        val usersDocumentReference =
            FirebaseFirestore
                .getInstance()
                .collection(User.PATH_USERS)
                .document(userId)

        val data: Map<String, Any> = hashMapOf(
            User.COINS to newCoins
        )
        usersDocumentReference.update(data).await()
    }
}