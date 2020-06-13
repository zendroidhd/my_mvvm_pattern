package com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment

import com.technologies.zenlight.mvvmtutorial.userInterface.base.BaseViewModel
import com.technologies.zenlight.mvvmtutorial.utils.NO_NETWORK_BODY
import com.technologies.zenlight.mvvmtutorial.utils.NO_NETWORK_TITLE

class HomeFragmentViewModel: BaseViewModel() {

    /******** Getters and Setters ********/

    var callbacks: HomeFragmentCallbacks? = null
    var dataModel: HomeFragmentDataModel? = null

    /******** OnClick Listeners *********/

    fun onShowToastClicked() = callbacks!!.onShowToastClicked()

    /******* DataModel Requests *********/

    fun requestUsers() {
        callbacks?.getActivityContext()?.let {activity->
            if (isConnectedToNetwork(activity)){
                dataModel!!.requestUsers(this)
            } else {
                callbacks!!.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}