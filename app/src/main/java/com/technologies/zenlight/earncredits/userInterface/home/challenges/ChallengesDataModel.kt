package com.technologies.zenlight.earncredits.userInterface.home.challenges

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.utils.CHALLENGES_COLLECTION
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import com.technologies.zenlight.earncredits.utils.pushUserProfileToObservers
import javax.inject.Inject

class ChallengesDataModel @Inject constructor(private val dataManager: AppDataManager) {


    fun getUserProfile(viewModel: ChallengesViewModel) {
        val userId = dataManager.getSharedPrefs().userId
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS_COLLECTION)
            .whereEqualTo("id", userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userProfiles = task.result!!.toObjects(UserProfile::class.java)
                    if (userProfiles.isNotEmpty()) {
                        viewModel.userProfile = userProfiles[0]
                        pushUserProfileToObservers(userProfiles[0])
                        getAllChallenges(viewModel)
                    }

                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    private fun getAllChallenges(viewModel: ChallengesViewModel) {
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
                    challengeArrayList.addAll(challenges)
                    if (challenges.isNotEmpty()) {
                        checkExpiredChallenges(viewModel, challengeArrayList)
                    } else {
                        viewModel.callbacks?.showNoChallengesFoundPage()
                    }

                } else {
                    val message = task.exception?.message ?: "Error retrieving the list of challenges"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    private fun checkExpiredChallenges(viewModel: ChallengesViewModel, challenges: ArrayList<Challenges>) {
        var totalCredits = viewModel.userProfile?.credits ?: 0
        viewModel.challengesList.clear()
        val difficulty = dataManager.getSharedPrefs().difficulty
        val filteredChallenges = ArrayList<Challenges>()

        if (difficulty != "easy") {
            for (challenge in challenges) {

                if (challenge.isDeleted) {
                    //removes deleted challenges older than a day
                    if (!challenge.wasCompletedToday()) {
                        removeChallenge(challenge)
                    }

                } else {
                    filteredChallenges.add(challenge)

                    if (challenge.getDaysLeftToComplete() < 0 && !challenge.isDeducted) {
                        challenge.isDeducted = true
                        if (difficulty == "normal") {
                            totalCredits -= challenge.credit
                        } else {
                            totalCredits = 0
                        }
                        updateChallenge(challenge)
                    }
                }
            }

            if (totalCredits < 0) {
                totalCredits = 0
            }

            viewModel.challengesList.addAll(filteredChallenges)

            viewModel.userProfile?.let {
                it.credits = totalCredits
                updateUserProfile(viewModel, it)
            }

        } else {

            for (challenge in challenges) {

                if (challenge.isDeleted) {
                    //removes deleted challenges older than a day
                    if (!challenge.wasCompletedToday()) {
                        removeChallenge(challenge)
                    }
                } else {
                    filteredChallenges.add(challenge)
                }

            }

            viewModel.challengesList.addAll(filteredChallenges)
            viewModel.callbacks?.onChallengesReturnedSuccessfully()
        }
    }

    private fun updateChallenge(challenge: Challenges) {
        val db = FirebaseFirestore.getInstance()
        db.collection(CHALLENGES_COLLECTION)
            .document(challenge.id)
            .set(challenge)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("TAG", "success")
                }
            }
    }

    private fun updateUserProfile(viewModel: ChallengesViewModel, userProfile: UserProfile) {
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS_COLLECTION)
            .document(userProfile.id)
            .set(userProfile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pushUserProfileToObservers(userProfile)
                    viewModel.callbacks?.onChallengesReturnedSuccessfully()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

    /**
     * This for when a user manually deletes a challenge. Their total credits is reduced by
     * the amount deleted
     */
    fun reduceCreditsForDeletedChallenge(viewModel: ChallengesViewModel, challenge: Challenges) {
        viewModel.userProfile?.let { profile ->
            val currentCredits = profile.credits
            var newCredits = currentCredits - challenge.credit
            if (newCredits < 0) {
                newCredits = 0
            }
            profile.credits = newCredits

            val db = FirebaseFirestore.getInstance()
            db.collection(USERS_COLLECTION)
                .document(profile.id)
                .set(profile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        pushUserProfileToObservers(profile)
                        setChallengeAsDeleted(viewModel, challenge)

                    } else {
                        val message = task.exception?.message ?: "Error updating profile"
                        viewModel.callbacks?.handleError("Error", message)
                    }
                }
        }
    }

    fun increaseCreditsForCompletedChallenge(viewModel: ChallengesViewModel, challenge: Challenges) {
        viewModel.userProfile?.let { profile ->
            val currentCredits = profile.credits
            val newCredits = currentCredits + challenge.credit

            profile.credits = newCredits

            val db = FirebaseFirestore.getInstance()
            db.collection(USERS_COLLECTION)
                .document(profile.id)
                .set(profile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        pushUserProfileToObservers(profile)
                        setChallengeAsDeleted(viewModel, challenge)

                    } else {
                        val message = task.exception?.message ?: "Error updating profile"
                        viewModel.callbacks?.handleError("Error", message)
                    }
                }
        }
    }

    /**
     * Removes the challenge from our Db
     */
    private fun removeChallenge(challenge: Challenges) {
        val db = FirebaseFirestore.getInstance()
        db.collection(CHALLENGES_COLLECTION)
            .document(challenge.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("TAG", "success")
                } else {
                    Log.i("TAG", "unable to delete")
                }
            }
    }

    /**
     * Removes the challenge from our Db
     */
    fun removeChallengeWithFeedback(viewModel: ChallengesViewModel,challenge: Challenges) {
        val db = FirebaseFirestore.getInstance()
        db.collection(CHALLENGES_COLLECTION)
            .document(challenge.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.challengesList.remove(challenge)
                    viewModel.callbacks?.onChallengesReturnedSuccessfully()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }


    /**
     * Sets the challenge status as deleted
     */
   private fun setChallengeAsDeleted(viewModel: ChallengesViewModel, challenge: Challenges) {
        val db = FirebaseFirestore.getInstance()
        val completedTimeStamp: Long = System.currentTimeMillis() / 1000
        challenge.actualCompletionDate = completedTimeStamp
        challenge.isDeleted = true
        db.collection(CHALLENGES_COLLECTION)
            .document(challenge.id)
            .set(challenge)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.challengesList.remove(challenge)
                    viewModel.callbacks?.onChallengesReturnedSuccessfully()
                } else {
                    val message = task.exception?.message ?: "Authentication Failed (F)"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }

}