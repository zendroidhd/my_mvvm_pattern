package com.technologies.zenlight.earncredits.userInterface.home.myProfile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.databinding.MyProfileLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivityCallbacks
import com.technologies.zenlight.earncredits.utils.showAlertDialog
import com.technologies.zenlight.earncredits.utils.showAlertDialogOneChoice
import javax.inject.Inject

class MyProfileFragment : BaseFragment<MyProfileLayoutBinding, MyProfileViewModel>(), MyProfileCallbacks {

    @Inject
    lateinit var dataModel: MyProfileDataModel

    private var parentCallbacks: HomeActivityCallbacks? = null

    override var viewModel: MyProfileViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.my_profile_layout

    companion object {
        fun newInstance(): MyProfileFragment {
            return MyProfileFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as HomeActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MyProfileViewModel::class.java)
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
        requestUserData()
    }

    override fun handleError(title: String, body: String) {
        parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity, title, body)
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun onSaveClicked() {
        val userName = dataBinding.etUserName.text.toString()
        val email = dataBinding.etEmail.text.toString()
        val passWord = dataBinding.etCode.text.toString()
        val reEnteredPassword = dataBinding.etReEnterCode.text.toString()
        viewModel?.let { it ->
            if (!it.isCredentialsValid(userName, email, passWord, reEnteredPassword)) {
                showAlertDialog(activity, "Invalid Credentials", it.getInvalidCredentialsText(userName, email, passWord, reEnteredPassword))
            } else {
                parentCallbacks?.showProgressSpinnerView()
                it.updateProfile(userName,email,passWord)
            }
        }
    }

    override fun onProfileDataUpdated() {
        parentCallbacks?.hideProgressSpinnerView()
        val msg = "Your profile has been updated"
        showAlertDialogOneChoice(activity,"",msg,"Ok", ::onBackPressed)
    }

    override fun onUserProfileReturned(userProfile: UserProfile) {
        parentCallbacks?.hideProgressSpinnerView()
        dataBinding.etUserName.setText(userProfile.userName)
        dataBinding.etCode.setText(userProfile.password)
        dataBinding.etEmail.setText(userProfile.email)
        dataBinding.etReEnterCode.setText(userProfile.password)
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }

    private fun requestUserData() {
        parentCallbacks?.showProgressSpinnerView()
        viewModel?.getUserProfile()
    }
}