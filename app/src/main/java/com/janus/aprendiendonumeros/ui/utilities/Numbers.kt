package com.janus.aprendiendonumeros.ui.utilities

import java.text.SimpleDateFormat
import java.util.*

class Numbers {

    companion object {

        fun getCurrentDate(timeZone: String): Date {
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            return date
        }
    }
}