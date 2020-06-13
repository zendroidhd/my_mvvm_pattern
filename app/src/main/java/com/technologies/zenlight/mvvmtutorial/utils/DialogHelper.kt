package com.technologies.zenlight.mvvmtutorial.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun showAlertDialog(activity: Activity?, title: String, message: String, negativeBtnText:String = "OK"){
    activity?.let {
        if (!it.isFinishing && !it.isDestroyed){
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeBtnText) {_, _ -> }
            dialog.show()
        }
    }
}

fun showToastLong(context: Context?, msg: String) {
    context.let { Toast.makeText(it, msg, Toast.LENGTH_LONG).show() }
}

fun showToastShort(context: Context?, msg: String) {
    context.let { Toast.makeText(it, msg, Toast.LENGTH_SHORT).show() }
}