package com.janus.aprendiendonumeros.data.model

data class Rank(
    val min: Int = 0,
    val max: Int
) {
    companion object {

        fun getRank(number: Int, rangeLimit: Int, list: List<Int>): Rank {
            return when {
                number <= rangeLimit -> Rank(number, number + (rangeLimit * 2))
                number <= (list.size - rangeLimit) -> Rank(
                    number - rangeLimit,
                    number + rangeLimit
                )
                else -> Rank(number - (rangeLimit * 2), number)
            }
        }
    }
}