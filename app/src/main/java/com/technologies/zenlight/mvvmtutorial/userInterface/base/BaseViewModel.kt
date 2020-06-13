package com.technologies.zenlight.mvvmtutorial.userInterface.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    private var mIsLoading = ObservableBoolean(false)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear();
    }

    fun getIsLoading(): ObservableBoolean {
        return mIsLoading
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    fun isConnectedToNetwork(context: Activity): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}