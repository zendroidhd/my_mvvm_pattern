package com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.createNewPowerup

import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.utils.POWERUPS_COLLECTION
import kotlinx.android.synthetic.main.power_ups_layout.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreatePowerupDataModel @Inject constructor(private val dataManager: AppDataManager) {

    fun createNewPowerup(viewModel: CreatePowerupViewModel, powerUp: PowerUps) {
        val context = dataManager.getAppContext()
        val mTimeStamp: Long = System.currentTimeMillis() / 1000
        val userId = dataManager.getSharedPrefs().userId ?: ""
        val db = FirebaseFirestore.getInstance()
        val newPowerUpRef = db.collection(POWERUPS_COLLECTION).document()
        val newPowerUpId: String = newPowerUpRef.id
        val itemsArray = ArrayList<String>(context.resources.getStringArray(R.array.powerups_item_list).toMutableList())
        val indexSize = itemsArray.size - 1
        val random = (0..indexSize).random()

        powerUp.id = newPowerUpId
        powerUp.authorId = userId
        powerUp.timestamp = mTimeStamp
        powerUp.icon = itemsArray[random]

        newPowerUpRef.set(powerUp)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.callbacks?.onPowerupCreatedSuccessfully()

                } else {
                    viewModel.callbacks?.handleError("Error","Authentication Failed")
                }
            }
    }
}