package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp

data class User(
    val nikname: String = "",
    val gender: String = "",
    val birthDate: Timestamp? = null,
    val level: Int = 0
)