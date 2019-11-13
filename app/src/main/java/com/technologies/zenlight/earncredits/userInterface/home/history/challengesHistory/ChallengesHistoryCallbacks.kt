package com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory

import android.app.Activity
import com.technologies.zenlight.earncredits.data.model.api.Challenges

interface ChallengesHistoryCallbacks {

    fun handleError(title: String, body: String)

    fun onChallengesReturnedSuccessfully()

    fun showNoChallengesFoundPage()

    fun getActivityContext(): Activity?
}