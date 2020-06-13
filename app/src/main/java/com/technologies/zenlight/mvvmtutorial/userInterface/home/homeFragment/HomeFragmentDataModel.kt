package com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment

import com.technologies.zenlight.mvvmtutorial.data.AppDataManager
import javax.inject.Inject

class HomeFragmentDataModel @Inject constructor(private val dataManager: AppDataManager) {

    fun requestUsers(viewModel: HomeFragmentViewModel) {
        viewModel.callbacks!!.onUsersReturned()
    }
}