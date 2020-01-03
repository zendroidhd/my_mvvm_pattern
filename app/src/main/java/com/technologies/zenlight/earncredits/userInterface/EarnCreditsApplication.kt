package com.technologies.zenlight.earncredits.userInterface

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.crashlytics.android.Crashlytics
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.dependencyInjection.component.DaggerApplicationComponent
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

class EarnCreditsApplication : Application(), HasActivityInjector, HasServiceInjector,HasSupportFragmentInjector, LifecycleObserver {

    companion object {
        var isAppDestroyed = false
    }

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var dataManager: AppDataManager

    override fun onCreate() {
        super.onCreate()
        initializeCrashlytics()
        initializeDagger()
    }

    private fun initializeDagger() {
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    private fun initializeCrashlytics() {
        Fabric.with(this, Crashlytics())
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

}