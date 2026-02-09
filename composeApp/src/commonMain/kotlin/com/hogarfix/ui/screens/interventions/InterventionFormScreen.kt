package com.hogarfix.ui.screens.interventions

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
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.Status
import com.hogarfix.ui.components.CategorySelector
import com.hogarfix.ui.components.CreateReminderDialog
import com.hogarfix.ui.components.DeleteConfirmationDialog
import com.hogarfix.ui.components.PhotoGallery
import com.hogarfix.ui.components.PhotoPickerResult
import com.hogarfix.ui.components.rememberPhotoPicker
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
fun InterventionFormScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InterventionFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var showReminderDialog by remember { mutableStateOf(false) }

    val photoPicker = rememberPhotoPicker { result: PhotoPickerResult ->
        viewModel.onEvent(InterventionFormEvent.PhotoAdded(result.bytes))
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is InterventionFormViewModel.NavigationEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is InterventionFormViewModel.NavigationEvent.ShowReminderDialog -> {
                    showReminderDialog = true
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(InterventionFormEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar intervencion" else "Nueva intervencion")
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
                    onValueChange = { viewModel.onEvent(InterventionFormEvent.TitleChanged(it)) },
                    label = { Text("Titulo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.onEvent(InterventionFormEvent.DescriptionChanged(it)) },
                    label = { Text("Descripcion") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                // Date
                OutlinedTextField(
                    value = state.date?.let { formatDate(it) } ?: "",
                    onValueChange = {},
                    label = { Text("Fecha *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(InterventionFormEvent.ShowDatePicker) },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                )

                // Category
                Text(
                    text = "Categoria *",
                    style = MaterialTheme.typography.labelLarge
                )
                CategorySelector(
                    selectedCategory = state.category,
                    onCategorySelected = { viewModel.onEvent(InterventionFormEvent.CategoryChanged(it)) }
                )

                // Status
                Text(
                    text = "Estado",
                    style = MaterialTheme.typography.labelLarge
                )
                StatusSelector(
                    selectedStatus = state.status,
                    onStatusSelected = { viewModel.onEvent(InterventionFormEvent.StatusChanged(it)) }
                )

                // Done By
                Text(
                    text = "Realizado por",
                    style = MaterialTheme.typography.labelLarge
                )
                DoneBySelector(
                    selectedDoneBy = state.doneBy,
                    onDoneBySelected = { viewModel.onEvent(InterventionFormEvent.DoneByChanged(it)) }
                )

                // Professional selector (only when done by professional)
                if (state.doneBy == DoneBy.PROFESSIONAL) {
                    ProfessionalSelector(
                        professionals = state.professionals,
                        selectedProfessional = state.selectedProfessional,
                        onProfessionalSelected = { professionalId ->
                            viewModel.onEvent(InterventionFormEvent.ProfessionalChanged(professionalId))
                        }
                    )
                }

                // Costs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.laborCost,
                        onValueChange = { viewModel.onEvent(InterventionFormEvent.LaborCostChanged(it)) },
                        label = { Text("Mano de obra (EUR)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.materialCost,
                        onValueChange = { viewModel.onEvent(InterventionFormEvent.MaterialCostChanged(it)) },
                        label = { Text("Materiales (EUR)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }

                // Photos
                Text(
                    text = "Fotos",
                    style = MaterialTheme.typography.labelLarge
                )
                PhotoGallery(
                    photoUris = state.photoUris,
                    onAddPhoto = { photoPicker.launch() },
                    onRemovePhoto = { uri -> viewModel.onEvent(InterventionFormEvent.PhotoRemoved(uri)) },
                    editable = true
                )

                // Notes
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { viewModel.onEvent(InterventionFormEvent.NotesChanged(it)) },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = { viewModel.onEvent(InterventionFormEvent.Save) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isValid && !state.isSaving && !state.isDeleting
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(if (state.isEditMode) "Guardar cambios" else "Crear intervencion")
                }

                // Delete button (only in edit mode)
                if (state.isEditMode) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(InterventionFormEvent.RequestDelete) },
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
                        Text("Eliminar intervencion")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Date Picker Dialog
    val currentDate = state.date
    if (state.showDatePicker && currentDate != null) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(InterventionFormEvent.HideDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            viewModel.onEvent(InterventionFormEvent.DateChanged(date))
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(InterventionFormEvent.HideDatePicker) }) {
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
            title = "Eliminar intervencion",
            message = "¿Estas seguro de que quieres eliminar \"${state.title}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(InterventionFormEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(InterventionFormEvent.CancelDelete) }
        )
    }

    // Reminder dialog after saving new intervention
    if (showReminderDialog) {
        CreateReminderDialog(
            onConfirm = { intervalDays ->
                showReminderDialog = false
                viewModel.createReminder(intervalDays)
            },
            onDismiss = {
                showReminderDialog = false
                viewModel.skipReminder()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusSelector(
    selectedStatus: Status,
    onStatusSelected: (Status) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Status.entries.forEach { status ->
            FilterChip(
                selected = status == selectedStatus,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        when (status) {
                            Status.PENDING -> "Pendiente"
                            Status.IN_PROGRESS -> "En curso"
                            Status.COMPLETED -> "Completado"
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoneBySelector(
    selectedDoneBy: DoneBy,
    onDoneBySelected: (DoneBy) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DoneBy.entries.forEach { doneBy ->
            FilterChip(
                selected = doneBy == selectedDoneBy,
                onClick = { onDoneBySelected(doneBy) },
                label = {
                    Text(
                        when (doneBy) {
                            DoneBy.MYSELF -> "Yo mismo"
                            DoneBy.PROFESSIONAL -> "Profesional"
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfessionalSelector(
    professionals: List<com.hogarfix.domain.model.Professional>,
    selectedProfessional: com.hogarfix.domain.model.Professional?,
    onProfessionalSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedProfessional?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Profesional") },
            placeholder = { Text("Seleccionar profesional") },
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
            // Option to clear selection
            if (selectedProfessional != null) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Sin asignar",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = {
                        onProfessionalSelected(null)
                        expanded = false
                    }
                )
            }

            if (professionals.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "No hay profesionales registrados",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = { expanded = false },
                    enabled = false
                )
            } else {
                professionals.forEach { professional ->
                    DropdownMenuItem(
                        text = { Text(professional.name) },
                        onClick = {
                            onProfessionalSelected(professional.id)
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
