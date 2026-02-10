package com.hogarfix.util

import platform.Foundation.NSUserDefaults

actual class AppPreferences actual constructor() {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun isOnboardingCompleted(): Boolean {
        return defaults.boolForKey("onboarding_completed")
    }

    actual fun setOnboardingCompleted(completed: Boolean) {
        defaults.setBool(completed, forKey = "onboarding_completed")
    }
}
