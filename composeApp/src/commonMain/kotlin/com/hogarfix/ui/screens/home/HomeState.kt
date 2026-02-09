package com.hogarfix.ui.screens.home

import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.model.Reminder

data class HomeState(
    val monthlyExpense: Double = 0.0,
    val pendingCount: Int = 0,
    val recentInterventions: List<Intervention> = emptyList(),
    val overdueRemindersCount: Int = 0,
    val upcomingReminders: List<Reminder> = emptyList(),
    val isLoading: Boolean = true
)
