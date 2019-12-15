package com.technologies.zenlight.earncredits.userInterface.home.suggestionBox

import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected

class SuggestionBoxViewModel: BaseViewModel() {

    /******** Getters and Setters **********/

    var callbacks: SuggestionBoxCallbacks? = null
    var dataModel: SuggestionBoxDataModel? = null

    /********* OnClick Listeners *********/

    fun onSubmitClicked() = callbacks?.onSubmitClicked()


    /******** DataModel Requests *********/

    fun sendSuggestion(text: String) {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.getUserProfile(this,text)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}