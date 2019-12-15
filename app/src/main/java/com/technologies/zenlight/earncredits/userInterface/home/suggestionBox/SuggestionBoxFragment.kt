package com.technologies.zenlight.earncredits.userInterface.home.suggestionBox

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.SuggestionBoxLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import com.technologies.zenlight.earncredits.utils.showAlertDialogOneChoice
import com.technologies.zenlight.earncredits.utils.showAlertDialogTwoChoices
import javax.inject.Inject

class SuggestionBoxFragment : BaseFragment<SuggestionBoxLayoutBinding, SuggestionBoxViewModel>(), SuggestionBoxCallbacks {

    @Inject
    lateinit var dataModel: SuggestionBoxDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null

    override var viewModel: SuggestionBoxViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.suggestion_box_layout


    companion object {
        fun newInstance(): SuggestionBoxFragment {
            return SuggestionBoxFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SuggestionBoxViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel?.dataModel = dataModel
        viewModel?.callbacks = this
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

    override fun onSuggestionSentSuccessfully() {
        parentCallbacks?.hideProgressSpinnerView()
        val title = "Suggestion Sent"
        val msg = "Thank you for your suggestion!"
        showAlertDialogOneChoice(activity,title,msg,"Ok",::onBackPressed)
    }

    override fun onSubmitClicked() {
        val text = dataBinding.etDescription.text.toString()
        val title = "Send Suggestion"
        val msg = "Are you sure you want to submit this suggestion?"
        if (text.isNotEmpty()) {
            showAlertDialogTwoChoices(activity,title,msg, "Cance", "Submit", ::submit)
        }
    }

    private fun submit() {
        val text = dataBinding.etDescription.text.toString()
        parentCallbacks?.showProgressSpinnerView()
        hideKeyboard()
        viewModel?.sendSuggestion(text)
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }
}