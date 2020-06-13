package com.technologies.zenlight.mvvmtutorial.userInterface.home.homeFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.mvvmtutorial.BR
import com.technologies.zenlight.mvvmtutorial.R
import com.technologies.zenlight.mvvmtutorial.databinding.FragmentContainerBinding
import com.technologies.zenlight.mvvmtutorial.databinding.HomeFragmentLayoutBinding
import com.technologies.zenlight.mvvmtutorial.userInterface.base.BaseFragment
import com.technologies.zenlight.mvvmtutorial.utils.showAlertDialog
import com.technologies.zenlight.mvvmtutorial.utils.showToastShort
import javax.inject.Inject

class HomeFragment: BaseFragment<HomeFragmentLayoutBinding, HomeFragmentViewModel>(), HomeFragmentCallbacks {

    @Inject
    lateinit var dataModel: HomeFragmentDataModel

    override var viewModel: HomeFragmentViewModel? = null
    override var bindingVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.home_fragment_layout


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel!!.callbacks = this
        viewModel!!.dataModel = dataModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel!!.requestUsers()
    }

    override fun onShowToastClicked() {
        showToastShort(context, "Shown successfully")
    }

    override fun onUsersReturned() {
        showToastShort(context, "Success")
    }

    override fun getActivityContext() = activity

    override fun handleError(title: String, body: String) {
        showAlertDialog(activity, title, body)
    }
}