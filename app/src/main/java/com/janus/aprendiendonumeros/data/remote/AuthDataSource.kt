package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class AuthDataSource {

    suspend fun signInAdult(nickName: String, passwordAdult: String): String {
        val usersCollectionReference =
            FirebaseFirestore.getInstance().collection(UserDataSource.PATH)
        val queryResult = usersCollectionReference
            .whereEqualTo(UserDataSource.NICK_NAME, nickName)
            .whereEqualTo(UserDataSource.PASSWORD_ADULT, passwordAdult)
            .limit(1)
            .get()
            .await()
        return queryResult.documents[0].id
    }

    suspend fun signInChild(passwordChild: String): String {
        val usersCollectionReference =
            FirebaseFirestore.getInstance().collection(UserDataSource.PATH)
        val queryResult = usersCollectionReference
            .whereEqualTo(UserDataSource.PASSWORD_CHILD, passwordChild)
            .limit(1)
            .get()
            .await()
        return queryResult.documents[0].id
    }

    suspend fun signUp(user: User) {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)

        val data = hashMapOf(
            UserDataSource.NICK_NAME to user.nickName,
            UserDataSource.GENDER to user.gender,
            UserDataSource.IMAGE to user.image,
            UserDataSource.PASSWORD_CHILD to user.passwordChild,
            UserDataSource.PASSWORD_ADULT to user.passwordAdult,
            UserDataSource.BIRTH_DATE to user.birthDate,
            UserDataSource.EMAIL to user.email,
            UserDataSource.COINS to user.coins
        )
        usersCollectionReference.document(user.id).set(data).await()
    }

    suspend fun fieldExistsInUser(field: String, value: Any): Boolean {
        val usersCollectionReference =
            FirebaseFirestore.getInstance().collection(UserDataSource.PATH)

        val queryResult = usersCollectionReference
            .whereEqualTo(field, value)
            .limit(1)
            .get().await()

        return queryResult.isEmpty.not()
    }

    suspend fun getUser(userId: String): User {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(UserDataSource.PATH)

        val userDocument = usersCollectionReference.document(userId).get().await()

        return User(
            id = userDocument.id,
            nickName = userDocument.data?.get(UserDataSource.NICK_NAME).toString(),
            gender = userDocument.data?.get(UserDataSource.GENDER).toString(),
            image = userDocument.data?.get(UserDataSource.IMAGE).toString(),
            passwordChild = "",
            passwordAdult = "",
            birthDate = userDocument.data?.get(UserDataSource.BIRTH_DATE) as Timestamp?,
            email = userDocument.data?.get(UserDataSource.EMAIL).toString(),
            coins = userDocument.data?.get(UserDataSource.COINS).toString().toInt()
        )
    }
}

