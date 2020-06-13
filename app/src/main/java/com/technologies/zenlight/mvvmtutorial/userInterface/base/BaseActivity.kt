package com.technologies.zenlight.mvvmtutorial.userInterface.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.technologies.zenlight.mvvmtutorial.data.appLevel.AppDataHelper
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V: ViewModel> : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var dataManager: AppDataHelper

    var loadingTag = "BaseActivity"
    protected val sdk = Build.VERSION.SDK_INT
    lateinit var dataBinding: T
        private set


    /**
     * Override for set dataBinding variable
     *
     * @return variable id
     */
    abstract var bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract var layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract var viewModel: V?



    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

   fun hideKeyboard() {
        val view = this.currentFocus
       view?.let {
           val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
           imm?.hideSoftInputFromWindow(it.windowToken, 0)
       }
    }

    private fun performDataBinding(){
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        dataBinding.setVariable(bindingVariable, viewModel)
        dataBinding.executePendingBindings()
    }
}