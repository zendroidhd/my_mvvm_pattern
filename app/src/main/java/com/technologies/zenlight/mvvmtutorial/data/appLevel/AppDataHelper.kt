package com.technologies.zenlight.mvvmtutorial.data.appLevel

import android.content.Context
import com.technologies.zenlight.mvvmtutorial.utils.AppSharedPrefs
import javax.inject.Inject

class AppDataHelper @Inject constructor(private val appContext : Context,
                                        private val sharedPrefs: AppSharedPrefs) : DataHelper {

    override fun getAppContext() = appContext

    override fun getSharedPrefs() = sharedPrefs
}