package com.janus.aprendiendonumeros.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileDataSource {

    suspend fun getUser(): Resource<User> {

        val documentSnapshot = FirebaseFirestore.getInstance().collection("users").document("Jimmy").get().await()

//        val userNickname: String = documentSnapshot.get("nickName") as String
//        val userGender: String = documentSnapshot.get("gender") as String
//        val userBirthDate: Timestamp = documentSnapshot.get("birthDate") as Timestamp
//        val userLevel: Int = documentSnapshot.get("nickName") as Int
//
//        val user = User(userNickname, userGender, userBirthDate, userLevel)

       documentSnapshot.data.let {
           val userNickname: String = documentSnapshot.get("nickName") as String
           val userGender: String = documentSnapshot.get("gender") as String
           val userBirthDate: Timestamp = documentSnapshot.get("birthDate") as Timestamp
           val userLevel: Int = documentSnapshot.get("nickName") as Int

           val user = User(userNickname, userGender, userBirthDate, userLevel)

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