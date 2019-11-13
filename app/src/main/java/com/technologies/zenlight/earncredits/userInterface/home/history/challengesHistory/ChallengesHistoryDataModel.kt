package com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory

import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.utils.CHALLENGES_COLLECTION
import javax.inject.Inject

class ChallengesHistoryDataModel @Inject constructor(private val dataManager: AppDataManager) {


    fun getTodaysCompletedChallenges(viewModel: ChallengesHistoryViewModel) {
        val userId = dataManager.getSharedPrefs().userId
        val db = FirebaseFirestore.getInstance()
        viewModel.challengesList.clear()

        db.collection(CHALLENGES_COLLECTION)
            .whereEqualTo("authorId", userId)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val challengeArrayList = ArrayList<Challenges>()
                    val challenges = task.result!!.toObjects(Challenges::class.java)

                    for (challenge in challenges) {
                        if (challenge.isDeleted && challenge.wasCompletedToday()) {
                            challengeArrayList.add(challenge)
                        }
                    }

                    viewModel.challengesList.addAll(challengeArrayList)

                    if (challengeArrayList.isEmpty()) {
                        viewModel.callbacks?.showNoChallengesFoundPage()
                    } else {
                        viewModel.callbacks?.onChallengesReturnedSuccessfully()
                    }

                } else {
                    val message = task.exception?.message ?: "Error retrieving the list of challenges"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }
}