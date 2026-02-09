package com.hogarfix.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openDialer(phoneNumber: String) {
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val url = NSURL.URLWithString("tel:$cleanNumber") ?: return
    UIApplication.sharedApplication.openURL(url)
}

actual fun openWhatsApp(phoneNumber: String) {
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val url = NSURL.URLWithString("https://wa.me/$cleanNumber") ?: return
    UIApplication.sharedApplication.openURL(url)
}
