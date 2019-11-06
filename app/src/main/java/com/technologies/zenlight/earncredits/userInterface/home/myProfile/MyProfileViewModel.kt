package com.technologies.zenlight.earncredits.userInterface.home.myProfile

import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected
import com.technologies.zenlight.earncredits.utils.isEmailValid

class MyProfileViewModel : BaseViewModel() {


    /******* Getters and Setters ***********/

    var callbacks: MyProfileCallbacks? = null
    var dataModel: MyProfileDataModel? = null
    var userProfile: UserProfile? = null


    /******* OnClick Listeners **********/

    fun onSaveClicked() = callbacks?.onSaveClicked()

    /******** Business Logic *********/

    fun isCredentialsValid(userName: String, email: String, password: String, reEnteredPassword: String): Boolean {
        return isEmailValid(email)
                && userName.isNotEmpty()
                && password.isNotEmpty()
                && reEnteredPassword.isNotEmpty()
                && password == reEnteredPassword
    }

    fun getInvalidCredentialsText(userName: String, email: String, password: String, reEnteredPassword: String): String {
        return if (!isEmailValid(email) && password.isEmpty()) {
            "Please enter a valid email and password"
        } else if (!isEmailValid(email)) {
            "Please enter a valid email"
        } else if (password != reEnteredPassword) {
            "Your passwords do not match"
        } else if (userName.isNotEmpty()) {
            "Please enter a valid userName"
        } else {
            "Please enter a valid password"
        }
    }

    /******** DataModel Requests **********/

    fun getUserProfile() {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.getUserProfile(this)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }

    fun updateProfile(userName: String, email: String, password: String) {
        callbacks?.getActivityContext()?.let { activity ->
            if (isConnected(activity)) {
                dataModel?.updateProfile(this,userName,email,password)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }
}