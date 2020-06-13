package com.technologies.zenlight.mvvmtutorial.dependencyInjection.component

import android.app.Application
import com.technologies.zenlight.mvvmtutorial.dependencyInjection.builder.ActivityBuilder
import com.technologies.zenlight.mvvmtutorial.dependencyInjection.builder.FragmentBuilder
import com.technologies.zenlight.mvvmtutorial.dependencyInjection.module.AppModule
import com.technologies.zenlight.mvvmtutorial.userInterface.AppApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class,
    ActivityBuilder::class, FragmentBuilder::class, AppModule::class])

interface ApplicationComponent {

     fun inject(app: AppApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}