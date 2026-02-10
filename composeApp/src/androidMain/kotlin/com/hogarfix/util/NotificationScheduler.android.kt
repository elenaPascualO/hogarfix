package com.hogarfix.util

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.hogarfix.domain.model.Reminder
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

private var notificationContext: Context? = null

fun initNotificationContext(context: Context) {
    notificationContext = context.applicationContext
    createNotificationChannel(context.applicationContext)
}

private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "hogarfix_reminders",
        "Recordatorios de mantenimiento",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Notificaciones de recordatorios de mantenimiento del hogar"
    }
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

actual class NotificationScheduler actual constructor() {

    actual fun schedule(reminder: Reminder) {
        val context = notificationContext ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            putExtra("reminder_id", reminder.id)
            putExtra("reminder_title", reminder.title)
            putExtra("reminder_description", reminder.description ?: "")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule at 9:00 AM on the due date
        val triggerTime = reminder.nextDueDate
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds() + (9 * 60 * 60 * 1000) // 9:00 AM

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } catch (_: SecurityException) {
            // Fallback: use inexact alarm if exact alarm permission not granted
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    actual fun cancel(reminderId: Long) {
        val context = notificationContext ?: return
        val intent = Intent(context, ReminderNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    actual fun requestPermission(onResult: (Boolean) -> Unit) {
        val context = notificationContext ?: run {
            onResult(false)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            onResult(granted)
        } else {
            onResult(true)
        }
    }
}
