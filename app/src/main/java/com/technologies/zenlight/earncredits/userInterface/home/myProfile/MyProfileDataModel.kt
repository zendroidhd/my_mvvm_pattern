package com.technologies.zenlight.earncredits.userInterface.home.myProfile

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import javax.inject.Inject

class MyProfileDataModel @Inject constructor(private val dataManager: AppDataManager) {


    fun getUserProfile(viewModel: MyProfileViewModel) {
        val userId = dataManager.getSharedPrefs().userId
        val db = FirebaseFirestore.getInstance()

        db.collection(USERS_COLLECTION)
            .whereEqualTo("id", userId)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val userProfiles = task.result!!.toObjects(UserProfile::class.java)
                    if (userProfiles.isNotEmpty()) {
                        val profile = userProfiles[0]
                        viewModel.userProfile = profile
                        viewModel.callbacks?.onUserProfileReturned(profile)
                    }

                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    fun updateProfile(viewModel: MyProfileViewModel, userName: String, email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        viewModel.userProfile?.let { profile ->

            profile.password = password
            profile.email = email
            profile.userName = userName

            db.collection(USERS_COLLECTION)
                .document(profile.id)
                .set(profile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reAuthenticateFirebase(email,password)
                        dataManager.getSharedPrefs().userEmail = email
                        dataManager.getSharedPrefs().userName = userName
                        dataManager.getSharedPrefs().userPassword = password
                        viewModel.callbacks?.onProfileDataUpdated()
                    } else {
                        val message = task.exception?.message ?: "Authentication Failed (F)"
                        viewModel.callbacks?.handleError("Error", message)
                    }
                }
        }
    }

    private fun reAuthenticateFirebase(newEmail: String, newPasswordString: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            it.updateEmail(newEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("TAG", "email updated")
                } else {
                    Log.e("TAG", "email failed to update")
                }
            }

            it.updatePassword(newPasswordString)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("TAG", "password updated")
                    } else {
                        Log.e("TAG", "password failed to update")
                    }
                }

        }
    }
}