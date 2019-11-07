package com.technologies.zenlight.earncredits.userInterface.home.gameOptions

import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel

class GameOptionsViewModel : BaseViewModel() {


    /********* Getters and Setters **********/

    var callbacks: GameOptionsCallbacks? = null
    var dataModel: GameOptionsDataModel? = null

    /********* OnClick Listener ************/

    fun onEasyClicked() = callbacks?.onEasyClicked()

    fun onNormalClicked() = callbacks?.onNormalClicked()

    fun onHardClicked() = callbacks?.onHardClicked()

    fun onSaveChangesClicked() = callbacks?.onSaveChangesClicked()

    /********* DataModel Requests **********/

    fun saveGameOptions(difficulty: Difficulty) {
        dataModel?.saveGameOptions(this,difficulty)
    }
}