package com.hogarfix.util

import com.hogarfix.domain.model.Reminder

expect class NotificationScheduler() {
    fun schedule(reminder: Reminder)
    fun cancel(reminderId: Long)
    fun requestPermission(onResult: (Boolean) -> Unit)
}
