package com.technologies.zenlight.mvvmtutorial.dependencyInjection.builder

import com.technologies.zenlight.mvvmtutorial.userInterface.home.homeActivity.HomeActivity
import com.technologies.zenlight.mvvmtutorial.userInterface.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindHomeActivity(): HomeActivity
}
