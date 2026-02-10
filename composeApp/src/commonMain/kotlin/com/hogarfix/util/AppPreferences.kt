package com.hogarfix.util

expect class AppPreferences() {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}
