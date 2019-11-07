package com.technologies.zenlight.earncredits.data.model.api

import com.google.firebase.firestore.Exclude
import com.technologies.zenlight.earncredits.utils.dateFormatter
import java.util.*

class Challenges {

    var id = ""
    var authorId = ""
    var description = ""
    var isComplete = false
    var isDeducted = false
    var credit = 0
    var createdOn: Long = 0
    var completeBy: Long = 0


    @Exclude
    fun getCompleteByDate(): String {
        val date = Date()
        date.time = completeBy * 1000
        return dateFormatter.format(date)
    }

    @Exclude
    fun getCreatedOnDate(): String {
        val date = Date()
        date.time = createdOn * 1000
        return dateFormatter.format(date)
    }

    @Exclude
    fun getDaysLeftToComplete(): Int {
        val today = Date().time
        val completedBy = completeBy * 1000
        val diff: Long = completedBy - today
        val numOfDays = (diff / (1000 * 60 * 60 * 24)).toInt()
        return numOfDays
    }
}