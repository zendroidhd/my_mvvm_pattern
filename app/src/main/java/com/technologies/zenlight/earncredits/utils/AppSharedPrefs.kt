package com.technologies.zenlight.earncredits.utils

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

    var userEmail: String?
        get() = sharedPreferences
            .getString(USER_EMAIL, "")
        set(id) = sharedPreferences
            .edit()
            .putString(USER_EMAIL, id)
            .apply()

    var userPassword: String?
        get() = sharedPreferences
            .getString(USER_PASSWORD, "")
        set(id) = sharedPreferences
            .edit()
            .putString(USER_PASSWORD, id)
            .apply()

    var userName: String?
        get() = sharedPreferences
            .getString(USER_NAME, "")
        set(id) = sharedPreferences
            .edit()
            .putString(USER_NAME, id)
            .apply()

    var quoteDayInYear: String?
        get() = sharedPreferences
            .getString(QUOTE_DAY_IN_YEAR, "")
        set(id) = sharedPreferences
            .edit()
            .putString(QUOTE_DAY_IN_YEAR, id)
            .apply()

    var savedQuote: String?
        get() = sharedPreferences
            .getString(SAVED_QUOTE, "")
        set(id) = sharedPreferences
            .edit()
            .putString(SAVED_QUOTE, id)
            .apply()

    var savedAuthor: String?
        get() = sharedPreferences
            .getString(SAVED_AUTHOR, "")
        set(id) = sharedPreferences
            .edit()
            .putString(SAVED_AUTHOR, id)
            .apply()

    var difficulty: String?
        get() = sharedPreferences
            .getString(DIFFICULTY_SELECTED, "")
        set(id) = sharedPreferences
            .edit()
            .putString(DIFFICULTY_SELECTED, id)
            .apply()

    var isLoggedIn: Boolean
        get() = sharedPreferences
            .getBoolean(USER_LOGGED_IN,false)
        set(loggedIn) = sharedPreferences
            .edit()
            .putBoolean(USER_LOGGED_IN,loggedIn)
            .apply()

    var hasSeenTutorial: Boolean
        get() = sharedPreferences
            .getBoolean(HAS_SEEN_TUTORIAL, false)
        set(loggedIn) = sharedPreferences
            .edit()
            .putBoolean(HAS_SEEN_TUTORIAL, loggedIn)
            .apply()


    companion object {
        private const val USER_NAME = "userName"
        private const val USER_EMAIL = "userEmail"
        private const val USER_PASSWORD = "userPassword"
        private const val USER_ID = "userId"
        private const val USER_LOGGED_IN = "userLoggedIn"
        private const val DIFFICULTY_SELECTED = "Difficulty"
        private const val QUOTE_DAY_IN_YEAR = "quoteDayInYear"
        private const val SAVED_QUOTE = "savedQuote"
        private const val SAVED_AUTHOR = "savedAuthor"
        private const val HAS_SEEN_TUTORIAL = "HasSeenTutorial"
        val FIREBASE_DEVICE_TOKEN = "fireBaseDeviceToken"
    }
}