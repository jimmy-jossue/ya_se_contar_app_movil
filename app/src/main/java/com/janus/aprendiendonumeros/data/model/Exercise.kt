package com.janus.aprendiendonumeros.data.model

class Exercise(
    val name: String = "",
    val image: String = "",
    val position: Int = 0,
    val status: Int = 0,
) {
    companion object {
        const val PATH_EXERCISES: String = "/exercises"
        const val NAME: String = "name"
        const val IMAGE: String = "image"
        const val POSITION: String = "position"
        const val STATUS: String = "status"
    }
}