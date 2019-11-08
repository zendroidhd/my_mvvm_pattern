package com.technologies.zenlight.earncredits.userInterface.home.mainMenu

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.technologies.zenlight.earncredits.data.AppDataManager
import com.technologies.zenlight.earncredits.data.model.api.Quotes
import com.technologies.zenlight.earncredits.utils.QUOTES_COLLECTION
import javax.inject.Inject

class MainMenuDataModel @Inject constructor(private val dataManager: AppDataManager) {


    fun getAllQuotes(viewModel: MainMenuViewModel) {
        val db = FirebaseFirestore.getInstance()
        db.collection(QUOTES_COLLECTION)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val quotes = task.result!!.toObjects(Quotes::class.java)

                    if (quotes.isNotEmpty()) {
                        val quoteArrayList = ArrayList<Quotes>()

                        for (quote in quotes) {
                            if (quote.title.isNotEmpty()) {
                               quoteArrayList.add(quote)
                            }
                        }

                        val sizeIndex = quoteArrayList.size - 1
                        val randomInt = (0..sizeIndex).random()
                        val selectedQuote = quoteArrayList[randomInt]
                        viewModel.callbacks?.onQuoteOfDayReturned(selectedQuote.title,selectedQuote.author)

                    } else {
                        viewModel.callbacks?.handleError("Error", "Error retrieving the quote of the day")
                    }

                } else {
                    val message = task.exception?.message ?: "Error retrieving the quote of the day"
                    viewModel.callbacks?.handleError("Error", message)
                }
            }
    }
}