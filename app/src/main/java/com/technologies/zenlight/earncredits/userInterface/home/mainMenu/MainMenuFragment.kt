package com.technologies.zenlight.earncredits.userInterface.home.mainMenu

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.MainMenuLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.gameOptions.GameOptionsFragment
import com.technologies.zenlight.earncredits.userInterface.home.history.HistoryPagerFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.leaderboards.LeaderboardsFragment
import com.technologies.zenlight.earncredits.userInterface.home.myProfile.MyProfileFragment
import com.technologies.zenlight.earncredits.userInterface.login.loginActivity.LoginActivity
import com.technologies.zenlight.earncredits.utils.*
import java.util.*
import javax.inject.Inject

class MainMenuFragment : BaseFragment<MainMenuLayoutBinding, MainMenuViewModel>(), MainMenuCallbacks {


    @Inject
    lateinit var dataModel: MainMenuDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null


    companion object {
        fun newInstance(): MainMenuFragment {
            return MainMenuFragment()
        }
    }

    override var viewModel: MainMenuViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.main_menu_layout


    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainMenuViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel?.callbacks = this
        viewModel?.dataModel = dataModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return dataBinding.root
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity, title, body)
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun onHistoryClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = HistoryPagerFragment.newInstance()
            addFragmentVertically(fragment, manager, "HistoryFragment", null)
        }
    }

    override fun onExitGameClicked() {
       showSignOutAlertDialog(activity, ::exitGame)
    }

    override fun onGameOptionsClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = GameOptionsFragment.newInstance()
            addFragmentVertically(fragment, manager, "GameOptions", null)
        }
    }

    override fun onLeaderBoardsClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = LeaderboardsFragment.newInstance()
            addFragmentVertically(fragment, manager, "Leaderboards", null)
        }
    }

    override fun onDailyCheatCodeClicked() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_YEAR).toString()
        val savedQuoteOfDay = dataManager.getSharedPrefs().quoteDayInYear
        val title = dataManager.getSharedPrefs().savedQuote ?: ""
        val author = dataManager.getSharedPrefs().savedAuthor ?: ""

        if (day == savedQuoteOfDay) {
            showQuoteOfDay(title,author)
        } else {
            parentCallbacks?.showProgressSpinnerView()
            viewModel?.getQuoteOfDay()
        }
    }

    override fun onNewQuotesClicked() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getQuoteOfDay()
    }

    override fun onMyProfileClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = MyProfileFragment.newInstance()
            addFragmentVertically(fragment, manager, "MyProfile", null)
        }
    }

    override fun onQuoteOfDayReturned(title: String, author: String) {
        parentCallbacks?.hideProgressSpinnerView()
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_YEAR).toString()
        dataManager.getSharedPrefs().quoteDayInYear = day
        dataManager.getSharedPrefs().savedAuthor = author
        dataManager.getSharedPrefs().savedQuote = title
        showQuoteOfDay(title,author)
    }

    private fun showQuoteOfDay(title: String, author: String) {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = QuoteFragment.newInstance(this,title,author)
            addFragmentFadeIn(fragment, manager, "QuoteFragment", null)
        }
    }


    private fun exitGame() {
        dataManager.getSharedPrefs().isLoggedIn = false
        activity?.let {
            it.finishAffinity()
            startActivity(LoginActivity.newIntent(it))
            showToastShort(activity,"Innovate, Create, and Keep Grinding")
        }
    }
}