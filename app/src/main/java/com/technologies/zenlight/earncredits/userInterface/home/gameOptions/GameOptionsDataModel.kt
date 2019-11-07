package com.technologies.zenlight.earncredits.userInterface.home.gameOptions

import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import javax.inject.Inject

class GameOptionsDataModel @Inject constructor(private val dataManager: AppDataManager) {

    fun saveGameOptions(viewModel: GameOptionsViewModel, difficulty: Difficulty) {
        getUserProfile(viewModel,difficulty)
    }

    private fun getUserProfile(viewModel: GameOptionsViewModel, difficulty: Difficulty) {
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
                        updateProfile(viewModel,profile,difficulty)
                    }

                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }

    }


    private fun updateProfile(viewModel: GameOptionsViewModel, userProfile: UserProfile,  difficulty: Difficulty) {
        val db = FirebaseFirestore.getInstance()
        val value: String = difficulty.name.toLowerCase()
        userProfile.difficulty = value

        db.collection(USERS_COLLECTION)
            .document(userProfile.id)
            .set(userProfile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataManager.getSharedPrefs().difficulty = value
                    viewModel.callbacks?.onProfileDataUpdated()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

}