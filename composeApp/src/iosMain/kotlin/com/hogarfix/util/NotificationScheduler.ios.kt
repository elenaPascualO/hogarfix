package com.hogarfix.util

import com.hogarfix.domain.model.Reminder
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

actual class NotificationScheduler actual constructor() {

    actual fun schedule(reminder: Reminder) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        val content = UNMutableNotificationContent().apply {
            setTitle(reminder.title)
            setBody(reminder.description ?: "")
        }

        val dateComponents = NSDateComponents().apply {
            year = reminder.nextDueDate.year.toLong()
            month = reminder.nextDueDate.monthNumber.toLong()
            day = reminder.nextDueDate.dayOfMonth.toLong()
            hour = 9
            minute = 0
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            "reminder_${reminder.id}",
            content,
            trigger
        )

        center.addNotificationRequest(request, withCompletionHandler = null)
    }

    actual fun cancel(reminderId: Long) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removePendingNotificationRequestsWithIdentifiers(listOf("reminder_$reminderId"))
    }

    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { granted, _ ->
            onResult(granted)
        }
    }
}
