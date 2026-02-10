package com.hogarfix.util

import android.content.Context
import android.content.SharedPreferences

private var appPreferencesContext: Context? = null

fun initAppPreferencesContext(context: Context) {
    appPreferencesContext = context.applicationContext
}

actual class AppPreferences actual constructor() {
    private val prefs: SharedPreferences?
        get() = appPreferencesContext?.getSharedPreferences("hogarfix_prefs", Context.MODE_PRIVATE)

    actual fun isOnboardingCompleted(): Boolean {
        return prefs?.getBoolean("onboarding_completed", false) ?: false
    }

    actual fun setOnboardingCompleted(completed: Boolean) {
        prefs?.edit()?.putBoolean("onboarding_completed", completed)?.apply()
    }
}
