package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp

data class User(
    var id: String = "",
    var nickName: String = "",
    var gender: String? = null,
    var image: String = "",
    var passwordChild: String = "",
    var passwordAdult: String = "",
    var birthDate: Timestamp? = null,
    var email: String? = null,
    var coins: Int = 0,
) {
    companion object {
        val staticInstance: User = User()
        const val PATH_USERS: String = "/users"
        const val NICK_NAME = "nickName"
        const val GENDER = "gender"
        const val IMAGE = "image"
        const val PASSWORD_CHILD = "passwordChild"
        const val PASSWORD_ADULT = "passwordAdult"
        const val BIRTH_DATE = "birthDate"
        const val EMAIL = "email"
        const val COINS = "coins"
    }
}