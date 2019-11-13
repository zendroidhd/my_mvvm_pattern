package com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory

import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.userInterface.home.challenges.ChallengesCallbacks
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected

class ChallengesHistoryViewModel : BaseViewModel() {


    /********** Getters and Setters ********/

    var callbacks: ChallengesHistoryCallbacks? = null
    var dataModel: ChallengesHistoryDataModel? = null
    var challengesList = ArrayList<Challenges>()

    /******** DataModel Requests **********/

    fun getTodaysCompletedChallenges() {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.getTodaysCompletedChallenges(this)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}