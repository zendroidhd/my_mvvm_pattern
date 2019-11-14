package com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory

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
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.databinding.PowerUpsHistoryLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.homeFragment.HomeFragmentViewModel
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import javax.inject.Inject

class PowerUpsHistoryFragment : BaseFragment<PowerUpsHistoryLayoutBinding, PowerUpsHistoryViewModel>(), PowerUpsHistoryCallbacks {


    @Inject
    lateinit var dataModel: PowerUpsHistoryDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null
    private var powerUpsList = ArrayList<PowerUps>()
    private var powerUpAdapter: PowerUpsHistoryAdapter? = null


    override var viewModel: PowerUpsHistoryViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.power_ups_history_layout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(PowerUpsHistoryViewModel::class.java)
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
        requestPowerUps()
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity,title,body)
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun onPowerUpsReturnedSuccessfully() {
        parentCallbacks?.hideProgressSpinnerView()
        viewModel?.let {
            powerUpsList.clear()
            powerUpsList.addAll(it.powerUpsList)
            powerUpsList.sortWith(Comparator { o1, o2 -> o1.actualUseDate.compareTo(o2.actualUseDate) })
            powerUpAdapter?.notifyDataSetChanged()
        }
    }

    override fun showNoPowerUpsFoundPage() {
        parentCallbacks?.hideProgressSpinnerView()
        dataBinding.layoutEmptyPowerUps.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        powerUpAdapter = PowerUpsHistoryAdapter(powerUpsList)
        dataBinding.rvPowerUps.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = powerUpAdapter
        }
    }

    private fun requestPowerUps() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getTodaysCompletedPowerUps()
    }
}