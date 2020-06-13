package com.technologies.zenlight.mvvmtutorial.userInterface.main

import com.technologies.zenlight.mvvmtutorial.userInterface.base.BaseViewModel

class MainActivityViewModel : BaseViewModel() {

    /*********** Getters and Setters *********/

    var callbacks : MainActivityCallbacks? = null


    /********** OnClick Listeners ********/

    fun onOpenFragmentClicked(){
        callbacks!!.onOpenFragmentClicked()
    }


    /********* Business Logic *********/

    fun isCorrectPasswordEntered(password: String, confirmedPassword: String) : Boolean {
        return password == confirmedPassword
    }

    /********* DataModel Requests ********/
}