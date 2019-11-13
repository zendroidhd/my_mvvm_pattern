package com.technologies.zenlight.earncredits.userInterface.home.mainMenu

import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected

class MainMenuViewModel: BaseViewModel() {

    /******** Getters and Setters ********/

    var callbacks: MainMenuCallbacks? = null
    var dataModel: MainMenuDataModel? = null

    /********* OnClick Listeners *********/

    fun onHistoryClicked() {
        callbacks?.onHistoryClicked()
    }

    fun onExitGameClicked(){
        callbacks?.onExitGameClicked()
    }

    fun onGameOptionsClicked() {
        callbacks?.onGameOptionsClicked()
    }

    fun onLeaderBoardsClicked() {
        callbacks?.onLeaderBoardsClicked()
    }

    fun onDailyCheatCodeClicked() {
        callbacks?.onDailyCheatCodeClicked()
    }

    fun onMyProfileClicked() {
        callbacks?.onMyProfileClicked()
    }


    /******** DataModel Requests *********/

    fun getQuoteOfDay() {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.getAllQuotes(this)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}