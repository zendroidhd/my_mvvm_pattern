package com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment

import android.app.Activity

interface HomeFragmentCallbacks {
    fun onUsersReturned()
    fun getActivityContext(): Activity?
    fun onShowToastClicked()
    fun handleError(title: String, body: String)
}