package com.technologies.zenlight.earncredits.userInterface.home.myProfile

import android.app.Activity
import com.technologies.zenlight.earncredits.data.model.api.UserProfile

interface MyProfileCallbacks {

    fun handleError(title: String, body: String)

    fun getActivityContext(): Activity?

    fun onSaveClicked()

    fun onUserProfileReturned(userProfile: UserProfile)

    fun onProfileDataUpdated()

}