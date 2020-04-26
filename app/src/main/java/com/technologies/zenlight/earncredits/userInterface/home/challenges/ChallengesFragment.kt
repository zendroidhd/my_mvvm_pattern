package com.technologies.zenlight.earncredits.userInterface.home.challenges

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.databinding.ChallengesLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.challenges.createNewChallenge.CreateChallengeFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.utils.*
import javax.inject.Inject

class ChallengesFragment: BaseFragment<ChallengesLayoutBinding, ChallengesViewModel>(), ChallengesCallbacks {

    @Inject
    lateinit var dataModel: ChallengesDataModel

    private var challengesAdapter: ChallengesAdapter? = null
    private val challengesList = ArrayList<Challenges>()
    private var parentCallbacks: HomeActivityCallbacks? = null
    private var paint: Paint? = null

    override var viewModel: ChallengesViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.challenges_layout

    companion object {
        fun newInstance() : ChallengesFragment {
            return ChallengesFragment()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ChallengesViewModel::class.java)
        viewModel?.callbacks = this
        viewModel?.dataModel = dataModel
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.setUpObservers()
        setUpRecyclerView()
        requestsChallenges()

        dataBinding.swipeContainer.setOnRefreshListener {
            parentCallbacks?.let {
                if (it.isLoading()) {
                    dataBinding.swipeContainer.isRefreshing = false
              } else {
                    viewModel?.getAllChallenges()
                }
            }
        }
    }

    override fun handleError(title: String, body: String) {
        hideAllProgressSpinners()
        showAlertDialog(activity,title,body)
    }

    override fun onChallengesReturnedSuccessfully() {
        hideAllProgressSpinners()
        dataBinding.layoutEmptyPowerUps.visibility = View.GONE
        challengesList.clear()
        viewModel?.let {
            challengesList.addAll(it.challengesList)
            challengesList.sortWith(Comparator { o1, o2 -> o1.completeBy.compareTo(o2.completeBy) })
            challengesAdapter?.notifyDataSetChanged()
        }

        if (challengesList.isEmpty()) {
            showNoChallengesFoundPage()
        }
    }

    override fun showNoChallengesFoundPage() {
        hideAllProgressSpinners()
        dataBinding.layoutEmptyPowerUps.visibility = View.VISIBLE
    }

    override fun onAddNewChallengeClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = CreateChallengeFragment.newInstance(this)
            addFragmentFadeIn(fragment,manager,"CreateChallenge",null)
        }
    }

    override fun onCompleteChallengeClicked(challenge: Challenges) {
        showCompleteChallengeAlertDialog(activity,challenge,::completeChallenge)
    }

    override fun onDeleteChallengeClicked(challenge: Challenges) {
        showDeleteChallengeAlertDialog(activity,challenge,::deleteChallenge)
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun requestsChallenges() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getAllChallenges()
    }

    private fun setUpRecyclerView() {
        challengesAdapter = ChallengesAdapter(challengesList, this)
        dataBinding.rvChallenges.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = challengesAdapter
        }
    }

    private fun deleteChallenge(challenge: Challenges) {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.deleteChallengeWithFeedback(challenge)
    }


    private fun hideAllProgressSpinners() {
        parentCallbacks?.hideProgressSpinnerView()
        dataBinding.swipeContainer.isRefreshing = false
    }

    private fun completeChallenge(challenge: Challenges) {
        viewModel?.completeChallenge(challenge)
    }

    private fun getPaint(): Paint {
        if (paint == null) {
            paint = Paint()
        }
        return paint!!
    }

}