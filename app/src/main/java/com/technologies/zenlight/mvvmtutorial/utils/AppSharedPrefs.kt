package com.technologies.zenlight.mvvmtutorial.utils

import android.content.SharedPreferences
import javax.inject.Inject

class AppSharedPrefs @Inject constructor(private val sharedPreferences: SharedPreferences) {

    var userId: String?
        get() = sharedPreferences
            .getString(USER_ID, "")
        set(id) = sharedPreferences
            .edit()
            .putString(USER_ID, id)
            .apply()


    companion object {
        private const val USER_ID = "userId"
    }
}