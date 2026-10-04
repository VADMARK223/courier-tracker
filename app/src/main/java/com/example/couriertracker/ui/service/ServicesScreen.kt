package com.example.couriertracker.ui.service

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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.ui.service.dialog.DeleteServiceDialog
import com.example.couriertracker.ui.service.list.ServicesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    onBack: () -> Unit,
    repository: DataRepository,
) {
    var serviceToDelete by remember { mutableStateOf<Service?>(null) }
    var showAddServiceSheet by remember { mutableStateOf(false) }

    val viewModel: ServicesViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ServicesViewModel(repository)
            }
        }
    )

    val services by viewModel.services.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Сервисы")
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
            FloatingActionButton(onClick = { showAddServiceSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить сервис")
            }
        }
    ) { innerPadding ->
        ServicesList(
            services = services,
            onServiceDeleteClick = { service ->
                serviceToDelete = service
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

    serviceToDelete?.let { service ->
        DeleteServiceDialog(
            service = service,
            onConfirm = {
                viewModel.deleteService(it)
                serviceToDelete = null
            },
            onDismiss = {
                serviceToDelete = null
            }
        )
    }

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
}