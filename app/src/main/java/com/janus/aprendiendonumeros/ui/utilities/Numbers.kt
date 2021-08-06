package com.janus.aprendiendonumeros.ui.utilities

class Numbers {

    companion object {
        fun getRandomNumberList(min: Int = 0, max: Int = 10): List<Int> {
            val list: MutableSet<Int> = mutableSetOf<Int>()
            var random: Int = (min..max).random()
            for (num: Int in min..max) {
                while (list.contains(random)) {
                    random = (min..max).random()
                }
                list.add(random)
            }
            return list.toList()
        }

        data class Rank(
            val min: Int = 0,
            val max: Int
        )
    }
}