package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp

data class LogActivity (
    val nameActivity: String = "",
    val corrects: Int = 0,
    val wrong: Int = 0,
    val timeSeconds: Int = 0,
    val date: Timestamp?= null
)