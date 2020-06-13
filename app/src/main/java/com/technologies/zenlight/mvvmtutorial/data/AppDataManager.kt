package com.technologies.zenlight.mvvmtutorial.data

import com.technologies.zenlight.mvvmtutorial.data.appLevel.AppDataHelper
import javax.inject.Inject

class AppDataManager @Inject constructor(private val appDataHelper: AppDataHelper) : DataManager {

    override fun getAppContext() = appDataHelper.getAppContext()

    override fun getSharedPrefs() = appDataHelper.getSharedPrefs()
}