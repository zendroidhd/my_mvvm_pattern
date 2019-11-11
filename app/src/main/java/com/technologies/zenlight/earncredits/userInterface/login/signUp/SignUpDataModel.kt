package com.technologies.zenlight.earncredits.userInterface.login.signUp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import com.technologies.zenlight.earncredits.utils.USERS_COLLECTION
import java.util.HashMap
import javax.inject.Inject

class SignUpDataModel @Inject constructor(private val dataManager: AppDataManager) {


    /**
     * Checks the user's email and password in our database
     */
    fun submitLoginCredentials(viewModel: SignUpViewModel,userName: String, email: String, password: String) {

        val db = FirebaseFirestore.getInstance()
        db.collection(USERS_COLLECTION)
            .whereEqualTo("email",email)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val userProfiles = task.result!!.toObjects(UserProfile::class.java)
                    if (userProfiles.isNotEmpty()) {
                        //the user already has an account
                        viewModel.callbacks?.showEmailAlreadyInUseAlert()
                    } else {
                        //add the user's details to the firestore db
                        createFirebaseUser(viewModel,userName,email,password)
                       // addUserToFirebaseDatabase(viewModel,email,password)
                    }

                } else {
                    val message = task.exception?.message?: "Please try again"
                    if (message.contains("insufficient permissions")) {
                        createFirebaseUser(viewModel,userName,email,password)
                    } else {
                        viewModel.callbacks?.handleError("Error", message)
                    }
                }
            }
    }

    /**
     * Creates a new user record in our Firebase database
     */
    private fun addUserToFirebaseDatabase(viewModel: SignUpViewModel, userName:String, email: String, password: String) {

        val db = FirebaseFirestore.getInstance()
        val timeStamp: Long = System.currentTimeMillis() / 1000
        val newUserRef = db.collection(USERS_COLLECTION).document()
        val newUserId: String = newUserRef.id

        val user = UserProfile()
        user.id  = newUserId
        user.email = email
        user.userName = userName
        user.password = password
        user.timestamp = timeStamp

        newUserRef.set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataManager.getSharedPrefs().userName = userName
                    dataManager.getSharedPrefs().userEmail = email
                    dataManager.getSharedPrefs().userPassword = password
                    dataManager.getSharedPrefs().userId = newUserId
                    viewModel.callbacks?.signUserIntoApp()

                } else {
                    viewModel.callbacks?.handleError("Error","Authentication Failed")
                }
            }
    }

    /**
     * Used if the user has never created an acouunt in Firebase Auth
     */
    fun createFirebaseUser(viewModel: SignUpViewModel, userName:String, email: String, password: String) {

        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // val user = mAuth.currentUser
                    addUserToFirebaseDatabase(viewModel,userName,email,password)
                } else {
                    val message = task.exception?.message?: "Authentication Failed"
                    viewModel.callbacks?.handleError("Error",message)
                }
            }
    }
}