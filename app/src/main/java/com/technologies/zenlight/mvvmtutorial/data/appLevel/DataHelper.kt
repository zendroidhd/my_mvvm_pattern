package com.technologies.zenlight.mvvmtutorial.data.appLevel

import android.content.Context
import com.technologies.zenlight.mvvmtutorial.utils.AppSharedPrefs

interface DataHelper {

    fun getAppContext() : Context

    fun getSharedPrefs(): AppSharedPrefs
}