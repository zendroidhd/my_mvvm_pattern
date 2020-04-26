package com.technologies.zenlight.earncredits.userInterface.login.loginFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.technologies.zenlight.earncredits.BR
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.LoginHomeScreenBinding
import com.technologies.zenlight.earncredits.userInterface.base.BaseFragment
import com.technologies.zenlight.earncredits.userInterface.home.homeActivity.HomeActivity
import com.technologies.zenlight.earncredits.userInterface.login.forgotPassword.ForgotPasswordFragment
import com.technologies.zenlight.earncredits.userInterface.login.loginActivity.LoginActivityCallbacks
import com.technologies.zenlight.earncredits.userInterface.login.signUp.SignUpFragment
import com.technologies.zenlight.earncredits.utils.*
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginHomeScreenBinding, LoginFragmentViewModel>(), LoginFragmentCallbacks {

    @Inject
    lateinit var dataModel: LoginFragmentDataModel


    private var parentCallbacks: LoginActivityCallbacks? = null

    override var viewModel: LoginFragmentViewModel? = null

    override var bindingVariable: Int = BR.viewModel

    override var layoutId: Int = R.layout.login_home_screen

    val client_id = "156426603330-86mvmp15m3l5jvgpt92lg6rr8ag2id2r.apps.googleusercontent.com"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentCallbacks = context as LoginActivityCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(LoginFragmentViewModel::class.java)
        super.onCreate(savedInstanceState)
        viewModel?.callbacks = this
        viewModel?.dataModel = dataModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        fadeInTitle()
        initializeGoogleSignIn()
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        populateSavedCredentials()
    }


    override fun handleError(title: String, body: String) {
       parentCallbacks?.hideProgressSpinnerView()
        showAlertDialog(activity,title,body)
    }

    override fun showSignUpAlert() {
        parentCallbacks?.hideProgressSpinnerView()
        showSignUpAlertDialog(activity,::onSignUpClicked)
    }

    override fun showForgotPasswordAlert() {
        parentCallbacks?.hideProgressSpinnerView()
        showForgotPasswordAlertDialog(activity,::onForgotPasswordClicked)
    }

    override fun onCredentialsReturnedSuccessfully() {
        val email = dataBinding.etEmail.text.toString()
        val passWord = dataBinding.etCode.text.toString()
        viewModel?.signUserIntoFirebase(email,passWord)
    }

    override fun signUserIntoApp() {
        parentCallbacks?.hideProgressSpinnerView()
        val email = dataBinding.etEmail.text.toString()
        val passWord = dataBinding.etCode.text.toString()

        dataManager.getSharedPrefs().isLoggedIn = true
        dataManager.getSharedPrefs().userEmail = email
        dataManager.getSharedPrefs().userPassword = passWord
        context?.let {
            baseActivity?.finish()
            baseActivity?.startActivity(HomeActivity.newIntent(it))
        }
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun onSignUpClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = SignUpFragment.newInstance()
            addFragmentFadeIn(fragment,manager,"SignUp",null)
        }
    }

    override fun onForgotPasswordClicked() {
        baseActivity?.let {
            val manager = it.supportFragmentManager
            val fragment = ForgotPasswordFragment.newInstance()
            addFragmentFadeIn(fragment,manager,"ForgotPassword",null)
        }

//        val intent = viewModel!!.googleSignInClient!!.signInIntent
//        startActivityForResult(intent, viewModel!!.GOOGLE_SIGN_IN)
    }

    override fun onEnterButtonClicked() {
        val email = dataBinding.etEmail.text.toString()
        val passWord = dataBinding.etCode.text.toString()
        viewModel?.let {
            if (!it.isLoginValuesValid(email, passWord)) {
                showAlertDialog(activity,"Invalid Credentials",it.getEmptyLoginValuesText(email,passWord))
            } else {
                parentCallbacks?.showProgressSpinnerView()
                it.submitLoginCredentials(email,passWord)
            }
        }
    }

    private fun populateSavedCredentials() {
        dataBinding.etEmail.setText(dataManager.getSharedPrefs().userEmail)
        dataBinding.etCode.setText(dataManager.getSharedPrefs().userPassword)
    }

    private fun fadeInTitle() {
        playSound()
        YoYo.with(Techniques.FadeIn)
            .duration(3000)
            .repeatMode(0)
            .onStart{ dataBinding.layoutTitle.visibility = View.VISIBLE}
            .onEnd{ fadeInContent() }
            .playOn(dataBinding.layoutTitle)
    }

    private fun fadeInContent() {
        YoYo.with(Techniques.FadeIn)
            .duration(1500)
            .repeatMode(0)
            .onStart { animator -> dataBinding.layoutContent.visibility = View.VISIBLE }
            .onEnd{ animator -> fadeInInsertCoin()  }
            .playOn(dataBinding.layoutContent)
    }

    private fun fadeInInsertCoin() {
        dataBinding.btnInsertCoin.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_fade_out))
    }

    private fun playSound(){
        val mediaPlayer : MediaPlayer = MediaPlayer.create(dataManager.getAppContext(), R.raw.opening_title)
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build())
        mediaPlayer.start()
    }

    private fun initializeGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(client_id)
            .build()
        viewModel!!.googleSignInClient = GoogleSignIn.getClient(baseActivity!!,gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel!!.GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            showToastShort(activity,"Approved")
        } catch (e: ApiException) {
            val msg = GoogleSignInStatusCodes.getStatusCodeString(e.statusCode)
            showToastShort(activity,msg)
        }
    }

}