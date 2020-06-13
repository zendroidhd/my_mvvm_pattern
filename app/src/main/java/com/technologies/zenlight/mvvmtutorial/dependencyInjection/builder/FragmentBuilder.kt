package com.technologies.zenlight.mvvmtutorial.dependencyInjection.builder


import com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {


    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment
}