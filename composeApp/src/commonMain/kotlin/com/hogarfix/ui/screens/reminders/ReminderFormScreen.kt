package com.hogarfix.ui.screens.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hogarfix.ui.components.CategorySelector
import com.hogarfix.ui.components.DeleteConfirmationDialog
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderFormScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ReminderFormViewModel.NavigationEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(ReminderFormEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar recordatorio" else "Nuevo recordatorio")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onEvent(ReminderFormEvent.TitleChanged(it)) },
                    label = { Text("Titulo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onEvent(ReminderFormEvent.DescriptionChanged(it)) },
                    label = { Text("Descripcion") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                // Category
                Text(
                    text = "Categoria *",
                    style = MaterialTheme.typography.labelLarge
                )
                CategorySelector(
                    selectedCategory = state.category,
                    onCategorySelected = { viewModel.onEvent(ReminderFormEvent.CategoryChanged(it)) }
                )

                // Interval picker
                Text(
                    text = "Repetir cada *",
                    style = MaterialTheme.typography.labelLarge
                )
                IntervalPicker(
                    value = state.intervalValue,
                    unit = state.intervalUnit,
                    onValueChanged = { viewModel.onEvent(ReminderFormEvent.IntervalValueChanged(it)) },
                    onUnitChanged = { viewModel.onEvent(ReminderFormEvent.IntervalUnitChanged(it)) }
                )

                // Next due date
                OutlinedTextField(
                    value = state.nextDueDate?.let { formatDate(it) } ?: "",
                    onValueChange = {},
                    label = { Text("Proxima fecha *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(ReminderFormEvent.ShowDatePicker) },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                )

                // Active toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activo",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = state.isActive,
                        onCheckedChange = { viewModel.onEvent(ReminderFormEvent.IsActiveChanged(it)) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = { viewModel.onEvent(ReminderFormEvent.Save) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isValid && !state.isSaving && !state.isDeleting
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(if (state.isEditMode) "Guardar cambios" else "Crear recordatorio")
                }

                // Delete button (only in edit mode)
                if (state.isEditMode) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(ReminderFormEvent.RequestDelete) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving && !state.isDeleting,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("Eliminar recordatorio")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Date Picker Dialog
    val currentDate = state.nextDueDate
    if (state.showDatePicker && currentDate != null) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(ReminderFormEvent.HideDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            viewModel.onEvent(ReminderFormEvent.NextDueDateChanged(date))
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ReminderFormEvent.HideDatePicker) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Delete confirmation dialog
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            title = "Eliminar recordatorio",
            message = "¿Estas seguro de que quieres eliminar \"${state.title}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(ReminderFormEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ReminderFormEvent.CancelDelete) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntervalPicker(
    value: Int,
    unit: IntervalUnit,
    onValueChanged: (Int) -> Unit,
    onUnitChanged: (IntervalUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Number input
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { text ->
                text.toIntOrNull()?.let { onValueChanged(it) }
            },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // Unit dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = unit.label,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                IntervalUnit.entries.forEach { intervalUnit ->
                    DropdownMenuItem(
                        text = { Text(intervalUnit.label) },
                        onClick = {
                            onUnitChanged(intervalUnit)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val format = LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }
    return date.format(format)
}
