package com.hogarfix.ui.screens.professionals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hogarfix.ui.components.CategorySelector
import com.hogarfix.ui.components.DeleteConfirmationDialog
import com.hogarfix.ui.components.RatingBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalFormScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfessionalFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfessionalFormViewModel.NavigationEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(ProfessionalFormEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar profesional" else "Nuevo profesional")
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
                    onValueChange = { viewModel.onEvent(ProfessionalFormEvent.NameChanged(it)) },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Phone
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { viewModel.onEvent(ProfessionalFormEvent.PhoneChanged(it)) },
                    label = { Text("Telefono") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(ProfessionalFormEvent.EmailChanged(it)) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                // Specialty
                Text(
                    text = "Especialidad *",
                    style = MaterialTheme.typography.labelLarge
                )
                CategorySelector(
                    selectedCategory = state.specialty,
                    onCategorySelected = { viewModel.onEvent(ProfessionalFormEvent.SpecialtyChanged(it)) }
                )

                // Rating
                Text(
                    text = "Valoracion",
                    style = MaterialTheme.typography.labelLarge
                )
                RatingBar(
                    rating = state.personalRating,
                    onRatingChanged = { viewModel.onEvent(ProfessionalFormEvent.RatingChanged(it)) },
                    starSize = 32.dp
                )

                // Notes
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { viewModel.onEvent(ProfessionalFormEvent.NotesChanged(it)) },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = { viewModel.onEvent(ProfessionalFormEvent.Save) },
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
                    Text(if (state.isEditMode) "Guardar cambios" else "Crear profesional")
                }

                // Delete button (only in edit mode)
                if (state.isEditMode) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(ProfessionalFormEvent.RequestDelete) },
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
                        Text("Eliminar profesional")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Delete confirmation dialog
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            title = "Eliminar profesional",
            message = "¿Estas seguro de que quieres eliminar \"${state.name}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(ProfessionalFormEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ProfessionalFormEvent.CancelDelete) }
        )
    }
}
