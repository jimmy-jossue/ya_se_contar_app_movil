package com.janus.aprendiendonumeros.ui.utilities

class Numbers {

    companion object {
        fun getRandomNumberList(length: Int, min: Int = 0, max: Int = 10): List<Int> {
            val list: MutableSet<Int> = mutableSetOf<Int>()
            var random: Int = (min..max).random()
            for (num: Int in 0..length) {
                while (list.contains(random)) {
                    random = (min..max).random()
                }
                list.add(random)
            }
            return list.toList()
        }
    }
}