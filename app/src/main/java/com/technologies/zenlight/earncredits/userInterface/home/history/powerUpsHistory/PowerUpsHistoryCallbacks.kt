package com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory

import android.app.Activity

interface PowerUpsHistoryCallbacks {

    fun handleError(title: String, body: String)

    fun showNoPowerUpsFoundPage()

    fun getActivityContext(): Activity?

    fun onPowerUpsReturnedSuccessfully()

}