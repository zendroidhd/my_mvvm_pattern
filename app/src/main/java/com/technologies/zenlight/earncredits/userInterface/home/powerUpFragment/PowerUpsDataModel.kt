package com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.userInterface.home.challenges.ChallengesViewModel
import com.technologies.zenlight.earncredits.utils.POWERUPS_COLLECTION
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import com.technologies.zenlight.earncredits.utils.pushUserProfileToObservers
import javax.inject.Inject

class PowerUpsDataModel @Inject constructor(private val dataManager: AppDataManager) {



    fun getAllPowerUps(viewModel: PowerUpsViewModel) {
        val userId = dataManager.getSharedPrefs().userId
        val db = FirebaseFirestore.getInstance()
        viewModel.powerUpsList.clear()
        db.collection(POWERUPS_COLLECTION)
            .whereEqualTo("authorId", userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val powerUps = task.result!!.toObjects(PowerUps::class.java)
                    if (powerUps.isNotEmpty()) {

                        for (powerUp in powerUps) {

                            if (powerUp.isDeleted) {
                                if (!powerUp.wasCompletedToday()) {
                                    removePowerup(powerUp)
                                }

                            } else {
                                viewModel.powerUpsList.add(powerUp)
                            }
                        }

                        viewModel.callbacks?.onPowerUpsReturnedSuccessfully()

                    } else {
                        viewModel.callbacks?.showNoPowerUpsFoundPage()
                    }

                } else {
                    val message = task.exception?.message ?: "Error retrieving the list of challenges"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }


    fun decreaseCreditsForUsedPowerup(viewModel: PowerUpsViewModel, powerUps: PowerUps) {
        viewModel.userProfile?.let { profile ->
            val currentCredits = profile.credits
            val newCredits = currentCredits - powerUps.cost

            if (newCredits < 0) {
                viewModel.callbacks?.showNotEnoughCreditsAlert()
            } else {
                profile.credits = newCredits
                val db = FirebaseFirestore.getInstance()
                db.collection(USERS_COLLECTION)
                    .document(profile.id)
                    .set(profile)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            pushUserProfileToObservers(profile)
                            viewModel.callbacks?.onPowerUpSuccessfullyUsed(powerUps)
                            setPowerupAsDeleted(viewModel,powerUps)

                        } else {
                            val message = task.exception?.message ?: "Error updating profile"
                            viewModel.callbacks?.handleError("Error", message)
                        }
                    }
            }
        }
    }



    private fun setPowerupAsDeleted(viewModel: PowerUpsViewModel, powerUps: PowerUps) {
        val completedTimeStamp: Long = System.currentTimeMillis() / 1000
        powerUps.isDeleted = true
        powerUps.actualUseDate = completedTimeStamp
        val db = FirebaseFirestore.getInstance()
        db.collection(POWERUPS_COLLECTION)
            .document(powerUps.id)
            .set(powerUps)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.powerUpsList.remove(powerUps)
                    viewModel.callbacks?.onPowerUpsReturnedSuccessfully()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    fun removePowerupWithFeedback(viewModel: PowerUpsViewModel,powerUps: PowerUps) {
        val db = FirebaseFirestore.getInstance()
        db.collection(POWERUPS_COLLECTION)
            .document(powerUps.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.powerUpsList.remove(powerUps)
                    viewModel.callbacks?.onPowerUpsReturnedSuccessfully()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    /**
     *
     */
    fun removePowerup(powerUps: PowerUps) {
        val db = FirebaseFirestore.getInstance()
        db.collection(POWERUPS_COLLECTION)
            .document(powerUps.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("TAG", "success")
                } else {
                    Log.i("TAG", "unable to delete")
                }
            }
    }
}