package com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory

import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.utils.POWERUPS_COLLECTION
import javax.inject.Inject

class PowerUpsHistoryDataModel @Inject constructor(private val dataManager: AppDataManager) {

    fun getTodaysCompletedPowerUps(viewModel: PowerUpsHistoryViewModel) {
        val userId = dataManager.getSharedPrefs().userId
        val db = FirebaseFirestore.getInstance()
        viewModel.powerUpsList.clear()

        db.collection(POWERUPS_COLLECTION)
            .whereEqualTo("authorId", userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val powerUpArrayList = ArrayList<PowerUps>()
                    val powerUps = task.result!!.toObjects(PowerUps::class.java)

                    for (powerUp in powerUps) {
                        if (powerUp.isDeleted && powerUp.wasCompletedToday()) {
                            powerUpArrayList.add(powerUp)
                        }
                    }

                    viewModel.powerUpsList.addAll(powerUpArrayList)

                    if (powerUpArrayList.isEmpty()) {
                        viewModel.callbacks?.showNoPowerUpsFoundPage()
                    } else {
                        viewModel.callbacks?.onPowerUpsReturnedSuccessfully()
                    }

                } else {
                    val message = task.exception?.message ?: "Error retrieving the list of challenges"
                    viewModel.callbacks?.handleError("Error", message)
                }

            }

    }
}