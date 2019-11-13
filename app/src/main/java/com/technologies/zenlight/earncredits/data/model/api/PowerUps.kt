package com.technologies.zenlight.earncredits.data.model.api

import com.google.firebase.firestore.Exclude
import com.technologies.zenlight.earncredits.utils.timeFormatter
import java.util.*

class PowerUps {

    var id = ""
    var authorId = ""
    var icon = ""
    var description = ""
    var isDeleted = false
    var cost = 0
    var timestamp: Long = 0
    var actualUseDate: Long = 0


    @Exclude
    fun wasCompletedToday(): Boolean {
        return if (actualUseDate == 0L) {
            false
        } else {
            val calendar = Calendar.getInstance()
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            calendar.timeInMillis = actualUseDate * 1000
            val completedDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

            completedDayOfYear == dayOfYear
        }
    }

    @Exclude
    fun getActualUseDate(): String {
        val date = Date()
        date.time = actualUseDate * 1000
        return timeFormatter.format(date)
    }
}