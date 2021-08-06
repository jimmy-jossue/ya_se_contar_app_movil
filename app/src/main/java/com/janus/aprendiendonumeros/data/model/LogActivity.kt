package com.janus.aprendiendonumeros.data.model

import com.google.firebase.Timestamp

data class LogActivity(
    var correct: Int = 0,
    var incorrect: Int = 0,
    val timeInSeconds: Int = 0,
    val date: Timestamp? = null
)