package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class AuthDataSource {

    suspend fun signInAdult(nickName: String, passwordAdult: String): String {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)
        val querySnapshot = refCollection
            .whereEqualTo(User.NICK_NAME, nickName)
            .whereEqualTo(User.PASSWORD_ADULT, passwordAdult)
            .limit(1)
            .get()
            .await()
        return querySnapshot.documents[0].id
    }

    suspend fun signInChild(passwordChild: String): String {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)
        val querySnapshot = refCollection
            .whereEqualTo(User.PASSWORD_CHILD, passwordChild)
            .limit(1)
            .get()
            .await()
        return querySnapshot.documents[0].id
    }

    suspend fun signUp(user: User) {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)

        val data = hashMapOf(
            User.NICK_NAME to user.nickName,
            User.GENDER to user.gender,
            User.IMAGE to user.image,
            User.PASSWORD_CHILD to user.passwordChild,
            User.PASSWORD_ADULT to user.passwordAdult,
            User.BIRTH_DATE to user.birthDate,
            User.EMAIL to user.email,
            User.COINS to user.coins
        )

        usersCollectionReference.document(user.id).set(data).await()
    }

    suspend fun fieldExistsInUser(field: String, value: Any): Boolean {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)

        val querySnapshot = refCollection
            .whereEqualTo(field, value)
            .limit(1)
            .get().await()

        return querySnapshot.isEmpty.not()
    }

    suspend fun getUser(userId: String): User {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)

        val userDocument = usersCollectionReference.document(userId).get().await()

        return User(
            id = userDocument.id,
            nickName = userDocument.data?.get(User.NICK_NAME).toString(),
            gender = userDocument.data?.get(User.GENDER).toString(),
            image = userDocument.data?.get(User.IMAGE).toString(),
            passwordChild = "",
            passwordAdult = "",
            birthDate = userDocument.data?.get(User.BIRTH_DATE) as Timestamp?,
            email = userDocument.data?.get(User.EMAIL).toString(),
            coins = userDocument.data?.get(User.COINS).toString().toInt()
        )
    }
}