package com.technologies.zenlight.earncredits.userInterface.home.suggestionBox

import android.app.Activity

interface SuggestionBoxCallbacks {

    fun handleError(title: String, body: String)

    fun getActivityContext(): Activity?

    fun onSuggestionSentSuccessfully()

    fun onSubmitClicked()
}