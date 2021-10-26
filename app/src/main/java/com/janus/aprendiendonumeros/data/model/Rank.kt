package com.janus.aprendiendonumeros.data.model

import com.janus.aprendiendonumeros.data.remote.FigureDataSource

data class Rank(
    val min: Int = 0,
    val max: Int,
) {
    companion object {
        private fun calculateRank(number: Int, rangeLimit: Int, listSize: Int): Rank {
            return when {
                number <= rangeLimit -> Rank(number, number + (rangeLimit * 2))
                number <= (listSize - rangeLimit) -> Rank(
                    number - rangeLimit,
                    number + rangeLimit
                )
                else -> Rank(number - (rangeLimit * 2), number)
            }
        }

        fun getRank(level: String, number: Int, listSize: Int): Rank {
            return when (level) {
                FigureDataSource.Level.FIRST.toString() -> Rank(1, listSize)
                FigureDataSource.Level.SECOND.toString() -> calculateRank(number, 5, listSize)
                else -> calculateRank(number, 2, listSize)
            }
        }
    }
}