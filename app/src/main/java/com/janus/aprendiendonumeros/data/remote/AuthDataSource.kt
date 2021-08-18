package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class AuthDataSource {

    suspend fun signInAdult(nickName: String, passwordAdult: String): String {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)

        val querySnapshot = refCollection
            .whereEqualTo(User.NICK_NAME, nickName)
            .whereEqualTo(User.PASSWORD_ADULT, passwordAdult)
            .limit(1)
            .get().await()

        return querySnapshot.documents[0].id
    }

    suspend fun signInChild(passwordChild: String): String {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)

        val querySnapshot = refCollection
            .whereEqualTo(User.PASSWORD_CHILD, passwordChild)
            .limit(1)
            .get().await()

        return querySnapshot.documents[0].id
    }

    suspend fun signUp(userId: String, user: User) {
        val usersCollectionReference = FirebaseFirestore
            .getInstance()
            .collection(User.PATH_USERS)
        usersCollectionReference.document(userId).set(user).await()
    }

    suspend fun fieldExistsInUser(field: String, value: Any): Boolean {
        val refCollection = FirebaseFirestore.getInstance().collection(User.PATH_USERS)

        val querySnapshot = refCollection
            .whereEqualTo(field, value)
            .limit(1)
            .get().await()

        return querySnapshot.isEmpty.not()
    }

    //suspend fun getUser(userId: String): User {
    //    val usersCollectionReference = FirebaseFirestore.getInstance().collection(
    //        User.PATH_USERS)
    //    val userDocument = usersCollectionReference.document(userId).get().await()
    //
    //    val user: User? = userDocument.toObject(User::class.java)
    //
    //    if (user != null) {
    //        return user
    //    } else {
    //        throw Exception()
    //    }
    //}

    suspend fun getUser(userId: String): User {
        val usersCollectionReference = FirebaseFirestore.getInstance().collection(
            User.PATH_USERS)
        val userDocument = usersCollectionReference.document(userId).get().await()

        return User(
            nickName = userDocument.data?.get(User.NICK_NAME).toString(),
            gender = userDocument.data?.get(User.GENDER).toString(),
            image = userDocument.data?.get(User.IMAGE).toString(),
            passwordChild = "",
            passwordAdult = "",
            birthDate = userDocument.data?.get(User.BIRTH_DATE) as Timestamp,
            email = userDocument.data?.get(User.EMAIL).toString(),
            coins = userDocument.data?.get(User.COINS).toString().toInt()
        )
    }

    suspend fun updateUser(user: User) {
        val usersCollectionReference = FirebaseFirestore.getInstance().collection(
            User.PATH_USERS)
        usersCollectionReference.document().set(user, SetOptions.merge()).await()
    }
}