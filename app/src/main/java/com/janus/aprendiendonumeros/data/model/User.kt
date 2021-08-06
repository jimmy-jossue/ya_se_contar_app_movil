package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val nickName: String = "",
    val gender: String? = null,
    val image: String = "",
    val passwordChild: String = "",
    val passwordAdult: String = "",
    val birthDate: Timestamp? = null,
    val email: String? = null,
    val coins: Int = 0
)