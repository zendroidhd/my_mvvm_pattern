package com.technologies.zenlight.earncredits.userInterface.login.signUp

import com.technologies.zenlight.earncredits.userInterface.base.BaseViewModel
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_BODY
import com.technologies.zenlight.earncredits.utils.NO_NETWORK_TITLE
import com.technologies.zenlight.earncredits.utils.isConnected
import com.technologies.zenlight.earncredits.utils.isEmailValid

class SignUpViewModel : BaseViewModel() {

    /*********** Getters and Setters ********/

    var dataModel: SignUpDataModel? = null
    var callbacks: SignUpCallbacks? = null

    /********* OnClick Listeners *********/

    fun onStartNewGameClicked () {
        callbacks?.onStartNewGameClicked()
    }

    fun onExitClicked() {
        callbacks?.onExitClicked()
    }

    /******* Business Logic ********/

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

    /********* DataModel Requests ********/

    fun submitLoginCredentials(userName: String,email: String, password: String) {
        callbacks?.getActivityContext()?.let {activity->
            if (isConnected(activity)){
                dataModel?.submitLoginCredentials(this, userName, email, password)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }

    fun createFirebaseUser(userName: String,email: String, password: String) {
        callbacks?.getActivityContext()?.let {activity->
            if (isConnected(activity)){
                dataModel?.createFirebaseUser(this,userName,email,password)
            } else {
                callbacks?.handleError(NO_NETWORK_TITLE, NO_NETWORK_BODY)
            }
        }
    }

}