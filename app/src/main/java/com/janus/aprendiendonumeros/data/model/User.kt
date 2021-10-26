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
        var staticInstance: User = User()
    }
}