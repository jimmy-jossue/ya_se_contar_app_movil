package com.janus.aprendiendonumeros.ui.utilities

import java.text.SimpleDateFormat
import java.util.*

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

        fun getCurrentDate(timeZone: String): Date {
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            return date
        }
    }
}