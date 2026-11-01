package com.example.couriertracker.ui.operation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.operation.list.OperationsList
import com.example.couriertracker.ui.service.sheet.AddServiceSheet
import com.example.couriertracker.ui.service.sheet.AddServiceSheetRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    onBack: () -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository
) {
    val viewModel: OperationsScreenViewModel = viewModel {
        OperationsScreenViewModel(
            repository = repository,
            settingsRepository = settingsRepository
        )
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val operations by repository.operationsWithCategory.collectAsStateWithLifecycle(emptyList())

    var showAddServiceSheet by remember { mutableStateOf(false) }
    var showAddOperationSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Операции")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.canAddOperation) {
                FloatingActionButton(
                    onClick = {
                        showAddOperationSheet = true
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить операцию"
                    )
                }
            }
        },
        content = { paddingValues ->
            if (!uiState.canAddOperation)
                OperationsSetupEmptyState(
                    uiState.hasServices,
                    uiState.hasCategories,
                    onAddService = {
                        showAddServiceSheet = true
                    },
                    onAddCategory = {
//                        showAddServiceSheet = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            else
                OperationsList(
                    operations = operations,
                    onOperationDelete = { operation -> viewModel.deleteOperation(operation) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
        }
    )

    if (showAddServiceSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddServiceSheet = false
            }
        ) {
            AddServiceSheet(
                onSave = { service ->
                    viewModel.saveService(service)
                    showAddServiceSheet = false
                }
            )
        }
    }

    if (showAddServiceSheet) {
        AddServiceSheetRoute(
            repository = repository,
            onDismiss = { showAddServiceSheet = false })
    }
}