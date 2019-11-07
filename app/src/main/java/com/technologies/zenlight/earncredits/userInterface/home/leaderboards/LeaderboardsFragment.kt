package com.technologies.zenlight.earncredits.userInterface.home.leaderboards

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.LeaderboardsLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.myProfile.MyProfileViewModel
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import javax.inject.Inject

class LeaderboardsFragment: BaseFragment<LeaderboardsLayoutBinding, LeaderboardsViewModel>(), LeaderboardsCallbacks {


    @Inject
    lateinit var dataModel: LeaderboardsDatamodel

    private var parentCallbacks: HomeActivityCallbacks? = null

    override var viewModel: LeaderboardsViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.leaderboards_layout

    companion object {
        fun newInstance(): LeaderboardsFragment {
            return LeaderboardsFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(LeaderboardsViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel?.callbacks = this
        viewModel?.dataModel = dataModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity,title,body)
    }
}