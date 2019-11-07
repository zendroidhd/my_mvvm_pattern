package com.technologies.zenlight.earncredits.userInterface.home.leaderboards

import android.app.Activity

interface LeaderboardsCallbacks {

    fun handleError(title: String, body: String)

    fun getActivityContext(): Activity?
}