package com.technologies.zenlight.earncredits.dependencyInjection.builder

import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivity
import com.technologies.zenlight.earncredits.userInterface.login.loginActivity.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindLoginActivity() : LoginActivity

    @ContributesAndroidInjector
    abstract fun bindHomeActivity(): HomeActivity
}