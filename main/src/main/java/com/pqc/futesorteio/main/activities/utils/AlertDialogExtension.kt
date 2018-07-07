package com.pqc.futesorteio.main.activities.utils

import android.content.Context
import android.support.v7.app.AlertDialog

fun showStandardAlert(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNeutralButton(android.R.string.ok, null).create()
            .show()
}