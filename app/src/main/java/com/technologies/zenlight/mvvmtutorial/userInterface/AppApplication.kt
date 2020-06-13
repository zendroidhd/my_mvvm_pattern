package com.technologies.zenlight.mvvmtutorial.userInterface

import android.app.Application
import com.technologies.zenlight.mvvmtutorial.data.AppDataManager
import com.technologies.zenlight.mvvmtutorial.dependencyInjection.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class AppApplication : Application(), HasAndroidInjector{


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var dataManager: AppDataManager

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private fun initializeDagger() {
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

}