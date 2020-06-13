package com.technologies.zenlight.mvvmtutorial.userInterface.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.mvvmtutorial.R
import com.technologies.zenlight.mvvmtutorial.BR
import com.technologies.zenlight.mvvmtutorial.databinding.MainLayoutBinding
import com.technologies.zenlight.mvvmtutorial.userInterface.base.BaseActivity
import com.technologies.zenlight.mvvmtutorial.userInterface.home.homeActivity.HomeActivity

class MainActivity : BaseActivity<MainLayoutBinding, MainActivityViewModel>(),
    MainActivityCallbacks {

    override var viewModel: MainActivityViewModel? = null
    override var bindingVariable: Int = BR.mainViewModel
    override var layoutId: Int = R.layout.main_layout


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel!!.callbacks = this
    }

    override fun onOpenFragmentClicked() {
        startActivity(HomeActivity.newIntent(this))
    }

}