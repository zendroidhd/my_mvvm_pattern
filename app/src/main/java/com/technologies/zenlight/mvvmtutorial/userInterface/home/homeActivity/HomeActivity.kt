package com.technologies.zenlight.mvvmtutorial.userInterface.home.homeActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.mvvmtutorial.R
import com.technologies.zenlight.mvvmtutorial.BR
import com.technologies.zenlight.mvvmtutorial.databinding.FragmentContainerBinding
import com.technologies.zenlight.mvvmtutorial.userInterface.base.BaseActivity
import com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment.HomeFragment

class HomeActivity: BaseActivity<FragmentContainerBinding, HomeActivityViewModel>() {

    override var viewModel: HomeActivityViewModel? = null
    override var bindingVariable: Int = 0
    override var layoutId: Int = R.layout.fragment_container


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
        super.onCreate(savedInstanceState)
        attachHomeFragment()
    }

    private fun attachHomeFragment() {
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = HomeFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    companion object {
        fun newIntent(context: Context) : Intent {
            return  Intent(context, HomeActivity::class.java)
        }
    }
}