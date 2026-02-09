package com.hogarfix.util

import android.content.Context
import android.content.Intent
import android.net.Uri

private var appContext: Context? = null

fun initPlatformActionsContext(context: Context) {
    appContext = context.applicationContext
}

actual fun openDialer(phoneNumber: String) {
    val context = appContext ?: return
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$cleanNumber")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

actual fun openWhatsApp(phoneNumber: String) {
    val context = appContext ?: return
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://wa.me/$cleanNumber")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
