package com.janus.aprendiendonumeros.data.model

class Exercise(
    val indexInMenu: Int = 0,
    val name: String = "",
    val image: String = "",
    val status: Int = 0,
    val position: Int = 1,
) {
    companion object {
        const val PATH_EXERCISES: String = "/exercises"
        const val INDEX_IN_MENU: String = "indexInMenu"
        const val NAME: String = "name"
        const val IMAGE: String = "image"
        const val STATUS: String = "status"
        const val POSITION: String = "position"

        const val NAME_KNOW_NUMBERS: String = "know_numbers"
        const val NAME_SELECT_AND_COUNT: String = "Select_and_count"
        const val NAME_MOVE_AND_COUNT: String = "Move_and_count"
        const val NAME_HOW_MANY: String = "How_many"
        const val NAME_LESS_OR_MORE: String = "Less_or_more"
        const val NAME_ORDER_AND_COUNT: String = "Order_and_count"
        //const val NAME_LOCKED: String = "locked"
    }
}