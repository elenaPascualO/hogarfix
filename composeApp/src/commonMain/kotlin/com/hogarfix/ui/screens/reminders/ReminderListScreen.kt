package com.hogarfix.ui.screens.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder
import com.hogarfix.ui.components.DeleteConfirmationDialog
import com.hogarfix.ui.components.EmptyStateView
import com.hogarfix.ui.components.ReminderCard
import com.hogarfix.ui.components.SwipeToDeleteContainer
import com.hogarfix.ui.components.getCategoryIcon
import com.hogarfix.ui.components.getCategoryLabel
import com.hogarfix.ui.theme.ReminderColors
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    onNavigateToForm: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ReminderListViewModel.NavigationEvent.NavigateToForm -> {
                    onNavigateToForm(event.reminderId)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(ReminderListEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Recordatorios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(ReminderListEvent.AddReminder) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Anadir recordatorio"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category filter
            CategoryFilterRow(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onEvent(ReminderListEvent.FilterByCategory(category))
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.isEmpty -> {
                    EmptyStateView(
                        icon = Icons.Outlined.NotificationsActive,
                        title = if (state.selectedCategory != null) {
                            "Sin recordatorios en ${getCategoryLabel(state.selectedCategory!!)}"
                        } else {
                            "Sin recordatorios"
                        },
                        message = if (state.selectedCategory != null) {
                            "No hay recordatorios en esta categoria"
                        } else {
                            "Programa tu primer recordatorio de mantenimiento"
                        },
                        actionLabel = if (state.selectedCategory == null) "Anadir recordatorio" else null,
                        onAction = if (state.selectedCategory == null) {
                            { viewModel.onEvent(ReminderListEvent.AddReminder) }
                        } else null
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Overdue section
                        if (state.overdueReminders.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Vencidos",
                                    count = state.overdueReminders.size,
                                    color = ReminderColors.Overdue
                                )
                            }
                            items(
                                items = state.overdueReminders,
                                key = { "overdue_${it.id}" }
                            ) { reminder ->
                                ReminderItem(
                                    reminder = reminder,
                                    viewModel = viewModel
                                )
                            }
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                        }

                        // Upcoming section
                        if (state.upcomingReminders.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Proximos",
                                    count = state.upcomingReminders.size,
                                    color = ReminderColors.Soon
                                )
                            }
                            items(
                                items = state.upcomingReminders,
                                key = { "upcoming_${it.id}" }
                            ) { reminder ->
                                ReminderItem(
                                    reminder = reminder,
                                    viewModel = viewModel
                                )
                            }
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                        }

                        // On track section
                        if (state.onTrackReminders.isNotEmpty()) {
                            item {
                                SectionHeader(
                                    title = "Programados",
                                    count = state.onTrackReminders.size,
                                    color = ReminderColors.OnTrack
                                )
                            }
                            items(
                                items = state.onTrackReminders,
                                key = { "ontrack_${it.id}" }
                            ) { reminder ->
                                ReminderItem(
                                    reminder = reminder,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    state.reminderToDelete?.let { reminder ->
        DeleteConfirmationDialog(
            title = "Eliminar recordatorio",
            message = "¿Estas seguro de que quieres eliminar \"${reminder.title}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(ReminderListEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ReminderListEvent.CancelDelete) }
        )
    }
}

@Composable
private fun ReminderItem(
    reminder: Reminder,
    viewModel: ReminderListViewModel
) {
    SwipeToDeleteContainer(
        onDelete = {
            viewModel.onEvent(ReminderListEvent.RequestDelete(reminder))
        }
    ) {
        ReminderCard(
            reminder = reminder,
            onClick = {
                viewModel.onEvent(ReminderListEvent.EditReminder(reminder))
            },
            onComplete = {
                viewModel.onEvent(ReminderListEvent.CompleteReminder(reminder.id))
            }
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$title ($count)",
        style = MaterialTheme.typography.titleSmall,
        color = color,
        modifier = modifier.padding(vertical = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFilterRow(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Todos") }
            )
        }
        items(Category.entries.size) { index ->
            val category = Category.entries[index]
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                label = { Text(getCategoryLabel(category)) }
            )
        }
    }
}
