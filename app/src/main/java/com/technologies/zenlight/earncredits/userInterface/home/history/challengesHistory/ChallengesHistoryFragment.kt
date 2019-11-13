package com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.databinding.ChallengesHistoryLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.homeFragment.HomeFragmentViewModel
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import javax.inject.Inject

class ChallengesHistoryFragment : BaseFragment<ChallengesHistoryLayoutBinding, ChallengesHistoryViewModel>(), ChallengesHistoryCallbacks {

    @Inject
    lateinit var dataModel: ChallengesHistoryDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null
    private val challengesList = ArrayList<Challenges>()
    private var challengesAdapter: ChallengesHistoryAdapter? = null

    override var viewModel: ChallengesHistoryViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.challenges_history_layout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ChallengesHistoryViewModel::class.java)
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
        setUpRecyclerView()
        requestChallenges()
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity,title,body)
    }

    override fun onChallengesReturnedSuccessfully() {
        parentCallbacks?.hideProgressSpinnerView()
        challengesList.clear()
        viewModel?.let {
            challengesList.addAll(it.challengesList)
            challengesAdapter?.notifyDataSetChanged()
        }
    }

    override fun showNoChallengesFoundPage() {
        parentCallbacks?.hideProgressSpinnerView()
        dataBinding.layoutEmptyPowerUps.visibility = View.VISIBLE
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    private fun setUpRecyclerView() {
        challengesAdapter = ChallengesHistoryAdapter(challengesList,this)
        dataBinding.rvChallenges.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = challengesAdapter
        }
    }

    private fun requestChallenges() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getTodaysCompletedChallenges()
    }
}