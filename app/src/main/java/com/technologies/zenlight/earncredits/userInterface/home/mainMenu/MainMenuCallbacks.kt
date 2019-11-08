package com.technologies.zenlight.earncredits.userInterface.home.mainMenu

import android.app.Activity

interface MainMenuCallbacks {

    fun onNewQuotesClicked()

    fun onMyProfileClicked()

    fun onExitGameClicked()

    fun onGameOptionsClicked()

    fun onLeaderBoardsClicked()

    fun onDailyCheatCodeClicked()

    fun getActivityContext(): Activity?

    fun handleError(title: String, body: String)

    fun onQuoteOfDayReturned(title: String, author: String)
}