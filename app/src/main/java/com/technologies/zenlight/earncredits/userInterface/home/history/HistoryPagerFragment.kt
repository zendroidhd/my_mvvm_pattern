package com.technologies.zenlight.earncredits.userInterface.home.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.HistoryLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.challenges.ChallengesFragment
import com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory.ChallengesHistoryFragment
import com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory.PowerUpsHistoryFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeFragment.HomeFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeFragment.HomeFragmentViewModel
import com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.PowerUpsFragment

class HistoryPagerFragment : BaseFragment<HistoryLayoutBinding, HistoryPagerViewModel>() {


    private var pagerAdapter: PagerAdapter? = null

    override var viewModel: HistoryPagerViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.history_layout

    companion object {
        fun newInstance() = HistoryPagerFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HistoryPagerViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            pagerAdapter = PagerAdapter(childFragmentManager)
        }
        dataBinding.vpMain.adapter = pagerAdapter
        dataBinding.viewpagertab.setupWithViewPager(dataBinding.vpMain)
    }

    private class PagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                ChallengesHistoryFragment()
            } else {
                PowerUpsHistoryFragment()
            }
        }
        override fun getCount() = 2
    }
}