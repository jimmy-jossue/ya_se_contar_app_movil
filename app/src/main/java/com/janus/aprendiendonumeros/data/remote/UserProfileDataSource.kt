package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileDataSource {

    companion object {
        const val PATH_USERS: String = "/users"
        const val FIELD_USER_ID = "id"
        const val FIELD_USER_NICK_NAME = "nickName"
        const val FIELD_USER_GENDER = "gender"
        const val FIELD_USER_IMAGE = "image"
        const val FIELD_USER_PASSWORD_CHILD = "passwordChild"
        const val FIELD_USER_PASSWORD_ADULT = "passwordAdult"
        const val FIELD_USER_BIRTH_DATE = "birthDate"
        const val FIELD_USER_EMAIL = "email"
        const val FIELD_USER_COINS = "coins"
    }

    suspend fun getUser(): Resource<User> {

        val documentSnapshot =
            FirebaseFirestore.getInstance().collection(PATH_USERS).document("Jimmy").get().await()
        documentSnapshot.data.let {
            val userNickname: String = documentSnapshot.get("nikname") as String
            val userGender: String = documentSnapshot.get("gender") as String
            val userBirthDate: Timestamp = documentSnapshot.get("birthDate") as Timestamp
            val userLevel: Int = documentSnapshot.get("nickName") as Int

            val user = User(
                "",
                userNickname,
                userGender,
                "",
                "",
                "",
                userBirthDate,
                "",
                0
            )

            return Resource.Success(user)
        }
    }

    suspend fun getUser(userId: String): Resource<User> {
        val usersCollectionReference = FirebaseFirestore.getInstance().collection(PATH_USERS)
        val userDocument = usersCollectionReference.document(userId).get().await()

        userDocument.data.let {
            val user: User = User(
                id = userDocument.get(FIELD_USER_ID) as String,
                nickName = userDocument.get(FIELD_USER_NICK_NAME) as String,
                gender = userDocument.get(FIELD_USER_GENDER) as String,
                image = userDocument.get(FIELD_USER_IMAGE) as String,
                passwordChild = "",
                passwordAdult = "",
                birthDate = userDocument.get(FIELD_USER_BIRTH_DATE) as Timestamp,
                email = userDocument.get(FIELD_USER_EMAIL) as String,
                coins = userDocument.get(FIELD_USER_COINS) as Int
            )
            return Resource.Success(user)
        }
    }

    suspend fun getUsers(): Resource<List<User>> {
        val list = mutableListOf<User>()
        val documentSnapshot = FirebaseFirestore.getInstance().collection("users").get().await()
        for (document in documentSnapshot.documents) {
            document.toObject(User::class.java)?.let {
                list.add(it)
            }
        }
        return Resource.Success(list)
    }


}