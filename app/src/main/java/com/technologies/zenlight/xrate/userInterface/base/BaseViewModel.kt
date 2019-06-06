package com.technologies.zenlight.xrate.userInterface.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
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
}