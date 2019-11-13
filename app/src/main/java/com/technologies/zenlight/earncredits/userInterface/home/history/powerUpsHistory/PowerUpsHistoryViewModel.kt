package com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory

import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected

class PowerUpsHistoryViewModel : BaseViewModel() {


    /*********** Getters and Setters ************/

    var callbacks: PowerUpsHistoryCallbacks? = null
    var dataModel: PowerUpsHistoryDataModel? = null
    var powerUpsList = ArrayList<PowerUps>()


    /********* DataModel Requests ***********/

    fun getTodaysCompletedPowerUps() {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.getTodaysCompletedPowerUps(this)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}