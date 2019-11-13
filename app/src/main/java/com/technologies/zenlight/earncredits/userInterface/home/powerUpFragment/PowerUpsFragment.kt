package com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.databinding.PowerUpsLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.createNewPowerup.CreateNewPowerupFragment
import com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.detailView.PowerUpDetailFragment
import com.technologies.zenlight.earncredits.utils.addFragmentFadeIn
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import javax.inject.Inject

class PowerUpsFragment: BaseFragment<PowerUpsLayoutBinding, PowerUpsViewModel>(), PowerUpsCallbacks {


    @Inject
    lateinit var dataModel: PowerUpsDataModel


    private var powerUpsAdapter: PowerUpsAdapter? = null
    private var powerUpsList = ArrayList<PowerUps>()
    private var parentCallbacks: HomeActivityCallbacks? = null

    override var viewModel: PowerUpsViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.power_ups_layout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(PowerUpsViewModel::class.java)
        viewModel?.callbacks = this
        viewModel?.dataModel = dataModel
        viewModel?.setUpObservers()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        requestPowerUps()

        dataBinding.swipeContainer.setOnRefreshListener {
            parentCallbacks?.let {
                if (it.isLoading()) {
                    dataBinding.swipeContainer.isRefreshing = false
                } else {
                    viewModel?.getAllPowerUps()
                }
            }
        }
    }

    override fun handleError(title: String, body: String) {
        hideAllProgressSpinners()
        showAlertDialog(activity,title,body)
    }

    override fun onPowerUpsReturnedSuccessfully() {
        hideAllProgressSpinners()
        dataBinding.layoutEmptyPowerUps.visibility = View.GONE
        powerUpsList.clear()
        viewModel?.let {
            powerUpsList.addAll(it.powerUpsList)
            powerUpsList.sortWith(Comparator { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) })
            powerUpsAdapter?.notifyDataSetChanged()
        }

        if (powerUpsList.isEmpty()) {
            showNoPowerUpsFoundPage()
        }
    }

    override fun showNotEnoughCreditsAlert() {
        hideAllProgressSpinners()
        val title = "You need more credits"
        val msg = "You don't have enough credits to use this power up, stay focused and keep grinding."
        showAlertDialog(activity,title,msg)
    }

    override fun showNoPowerUpsFoundPage() {
        hideAllProgressSpinners()
        dataBinding.layoutEmptyPowerUps.visibility = View.VISIBLE
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun onPowerUpListRowClicked(powerUps: PowerUps) {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = PowerUpDetailFragment.newInstance(this,powerUps)
            addFragmentFadeIn(fragment,manager,"PowerUpDetails",null)
        }
    }

    override fun onAddNewPowerUpClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = CreateNewPowerupFragment.newInstance(this)
            addFragmentFadeIn(fragment,manager,"CreatePowerup",null)
        }
    }

    override fun onCompletePowerUpClicked(powerUps: PowerUps) {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.decreaseCreditsForUsedPowerup(powerUps)
    }

    override fun onDeletePowerUpClicked(powerUps: PowerUps) {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.setPowerupAsDeleted(powerUps)
    }

    override fun requestPowerUps() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getAllPowerUps()
    }

    override fun onPowerUpSuccessfullyUsed(powerUps: PowerUps) {
        hideAllProgressSpinners()
        Log.d("Powerup","successfully used")
    }


    private fun setUpRecyclerView() {
        powerUpsAdapter = PowerUpsAdapter(powerUpsList,this)
        dataBinding.rvPowerUps.run {
            layoutManager = GridLayoutManager(activity,2)
            adapter = powerUpsAdapter
        }
    }

    private fun hideAllProgressSpinners() {
        parentCallbacks?.hideProgressSpinnerView()
        dataBinding.swipeContainer.isRefreshing = false
    }
}