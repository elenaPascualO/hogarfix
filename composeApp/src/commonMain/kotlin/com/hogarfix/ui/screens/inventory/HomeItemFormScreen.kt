package com.hogarfix.ui.screens.inventory

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hogarfix.ui.components.CategorySelector
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
fun HomeItemFormScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeItemFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    val photoPicker = rememberPhotoPicker { result: PhotoPickerResult ->
        viewModel.onEvent(HomeItemFormEvent.PhotoAdded(result.bytes))
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is HomeItemFormViewModel.NavigationEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(HomeItemFormEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar elemento" else "Nuevo elemento")
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
                // Name
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onEvent(HomeItemFormEvent.NameChanged(it)) },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Brand
                OutlinedTextField(
                    value = state.brand,
                    onValueChange = { viewModel.onEvent(HomeItemFormEvent.BrandChanged(it)) },
                    label = { Text("Marca") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Model
                OutlinedTextField(
                    value = state.model,
                    onValueChange = { viewModel.onEvent(HomeItemFormEvent.ModelChanged(it)) },
                    label = { Text("Modelo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Category
                Text(
                    text = "Categoria *",
                    style = MaterialTheme.typography.labelLarge
                )
                CategorySelector(
                    selectedCategory = state.category,
                    onCategorySelected = { viewModel.onEvent(HomeItemFormEvent.CategoryChanged(it)) }
                )

                // Purchase Date
                DateFieldWithClear(
                    label = "Fecha de compra",
                    value = state.purchaseDate,
                    onShowPicker = { viewModel.onEvent(HomeItemFormEvent.ShowPurchaseDatePicker) },
                    onClear = { viewModel.onEvent(HomeItemFormEvent.ClearPurchaseDate) }
                )

                // Warranty End Date
                DateFieldWithClear(
                    label = "Fin de garantia",
                    value = state.warrantyEndDate,
                    onShowPicker = { viewModel.onEvent(HomeItemFormEvent.ShowWarrantyDatePicker) },
                    onClear = { viewModel.onEvent(HomeItemFormEvent.ClearWarrantyDate) }
                )

                // Location
                OutlinedTextField(
                    value = state.location,
                    onValueChange = { viewModel.onEvent(HomeItemFormEvent.LocationChanged(it)) },
                    label = { Text("Ubicacion") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Photos
                Text(
                    text = "Fotos",
                    style = MaterialTheme.typography.labelLarge
                )
                PhotoGallery(
                    photoUris = state.photoUris,
                    onAddPhoto = { photoPicker.launch() },
                    onRemovePhoto = { uri -> viewModel.onEvent(HomeItemFormEvent.PhotoRemoved(uri)) },
                    editable = true
                )

                // Notes
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { viewModel.onEvent(HomeItemFormEvent.NotesChanged(it)) },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = { viewModel.onEvent(HomeItemFormEvent.Save) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isValid && !state.isSaving && !state.isDeleting
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(if (state.isEditMode) "Guardar cambios" else "Crear elemento")
                }

                // Delete button (only in edit mode)
                if (state.isEditMode) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(HomeItemFormEvent.RequestDelete) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving && !state.isDeleting,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("Eliminar elemento")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Purchase Date Picker Dialog
    if (state.showPurchaseDatePicker) {
        val initialMillis = state.purchaseDate?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(HomeItemFormEvent.HidePurchaseDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            viewModel.onEvent(HomeItemFormEvent.PurchaseDateChanged(date))
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(HomeItemFormEvent.HidePurchaseDatePicker) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Warranty Date Picker Dialog
    if (state.showWarrantyDatePicker) {
        val initialMillis = state.warrantyEndDate?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(HomeItemFormEvent.HideWarrantyDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            viewModel.onEvent(HomeItemFormEvent.WarrantyEndDateChanged(date))
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(HomeItemFormEvent.HideWarrantyDatePicker) }) {
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
            title = "Eliminar elemento",
            message = "¿Estas seguro de que quieres eliminar \"${state.name}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(HomeItemFormEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(HomeItemFormEvent.CancelDelete) }
        )
    }
}

@Composable
private fun DateFieldWithClear(
    label: String,
    value: LocalDate?,
    onShowPicker: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value?.let { formatDate(it) } ?: "",
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShowPicker() },
        enabled = false,
        trailingIcon = {
            Row {
                if (value != null) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar fecha"
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }
    )
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
