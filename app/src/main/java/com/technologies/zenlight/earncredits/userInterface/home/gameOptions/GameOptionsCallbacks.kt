package com.technologies.zenlight.earncredits.userInterface.home.gameOptions

import android.app.Activity

interface GameOptionsCallbacks {

    fun handleError(title: String, body: String)

    fun getActivityContext(): Activity?

    fun onSaveChangesClicked()

    fun onProfileDataUpdated()

    fun onEasyClicked()

    fun onNormalClicked()

    fun onHardClicked()
}