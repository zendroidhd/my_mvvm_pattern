package com.technologies.zenlight.earncredits.userInterface.home.gameOptions

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.GameOptionsLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.home.myProfile.MyProfileFragment
import com.technologies.zenlight.earncredits.userInterface.home.myProfile.MyProfileViewModel
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import com.technologies.zenlight.earncredits.utils.showAlertDialogOneChoice
import javax.inject.Inject

class GameOptionsFragment : BaseFragment<GameOptionsLayoutBinding, GameOptionsViewModel>(), GameOptionsCallbacks {

    @Inject
    lateinit var dataModel: GameOptionsDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null
    private var selectedDifficulty = Difficulty.EASY

    override var viewModel: GameOptionsViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.game_options_layout

    companion object {
        fun newInstance(): GameOptionsFragment {
            return GameOptionsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(GameOptionsViewModel::class.java)
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
        setDifficulty()
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity, title, body)
    }

    override fun onProfileDataUpdated() {
        parentCallbacks?.hideProgressSpinnerView()
        val msg = "Your profile has been updated"
        showAlertDialogOneChoice(activity,"",msg,"Ok", ::onBackPressed)
    }

    override fun onSaveChangesClicked() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.saveGameOptions(selectedDifficulty)
    }

    override fun onEasyClicked() {
        val msg = "You will not lose any credits when your challenges expire"
        dataBinding.tvDescription.text = msg
        selectedDifficulty = Difficulty.EASY
        dataBinding.btnEasy.setTextColor(ContextCompat.getColor(context!!, R.color.pac_yellow))
        dataBinding.btnNormal.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
        dataBinding.btnHard.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
    }

    override fun onNormalClicked() {
        val msg = "When a challenge expires, you will lose credits equal to the value of the challenge (unless you have zero credits)"
        dataBinding.tvDescription.text = msg
        selectedDifficulty = Difficulty.NORMAL
        dataBinding.btnEasy.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
        dataBinding.btnNormal.setTextColor(ContextCompat.getColor(context!!, R.color.pac_yellow))
        dataBinding.btnHard.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
    }

    override fun onHardClicked() {
        val msg = "When a challenge expires, you will lose all of your credits (unless you have zero credits)"
        dataBinding.tvDescription.text = msg
        selectedDifficulty = Difficulty.HARD
        dataBinding.btnEasy.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
        dataBinding.btnNormal.setTextColor(ContextCompat.getColor(context!!, R.color.transparent_white))
        dataBinding.btnHard.setTextColor(ContextCompat.getColor(context!!, R.color.pac_yellow))
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }

    private fun setDifficulty() {
        when (dataManager.getSharedPrefs().difficulty) {
            Difficulty.EASY.name.toLowerCase() -> onEasyClicked()
            Difficulty.NORMAL.name.toLowerCase() -> onNormalClicked()
            else -> onHardClicked()
        }
    }
}