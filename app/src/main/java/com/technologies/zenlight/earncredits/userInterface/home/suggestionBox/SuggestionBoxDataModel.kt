package com.technologies.zenlight.earncredits.userInterface.home.suggestionBox

import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.Suggestion
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.utils.SUGGESTIONS_COLLECION
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import javax.inject.Inject

class SuggestionBoxDataModel @Inject constructor(private val dataManager: AppDataManager) {


    fun getUserProfile(viewModel: SuggestionBoxViewModel, text: String) {
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
                        sendSuggestion(viewModel, text, profile)

                    } else {
                        //should never happen at this point
                        viewModel.callbacks?.handleError("Error", "Error retrieving profile details")
                    }

                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    private fun sendSuggestion(viewModel: SuggestionBoxViewModel, text: String, profile: UserProfile) {
        val db = FirebaseFirestore.getInstance()
        val currentTimeStamp: Long = System.currentTimeMillis() / 1000
        val suggestionRef = db.collection(SUGGESTIONS_COLLECION).document()
        val newSuggestionId: String = suggestionRef.id
        val suggestion = Suggestion()

        suggestion.apply {
            id = newSuggestionId
            authorId = profile.id
            suggestion.text = text
            authorName = profile.userName
            timestamp = currentTimeStamp
        }

        suggestionRef.set(suggestion)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    viewModel.callbacks?.onSuggestionSentSuccessfully()

                } else {
                    val message = task.exception?.message ?: "Your suggestion cannot be submitted at this time, please try again later"
                    viewModel.callbacks?.handleError("Error", message)

                }
            }
    }
}