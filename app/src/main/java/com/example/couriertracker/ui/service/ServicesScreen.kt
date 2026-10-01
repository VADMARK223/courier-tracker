package com.example.couriertracker.ui.service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    onBack: () -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository,
    onSave: (Service) -> Unit,
    onServiceDelete: (Service) -> Unit,
) {
    var serviceToDelete by remember { mutableStateOf<Service?>(null) }
    var showAddSlotSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
                Text(
                    text = "Список сервисов",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddSlotSheet = true
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить слот"
                )
            }
        },
        content = { paddingValues ->
            ServicesList(
                repository = repository,
                settingsRepository = settingsRepository,
                onServiceDelete = { service ->
                    serviceToDelete = service
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )

    serviceToDelete?.let { service ->
        AlertDialog(
            onDismissRequest = { serviceToDelete = null }, // Закрываем при клике мимо
            title = { Text(text = "Удаление сервиса") },
            text = { Text(text = "Вы уверены, что хотите удалить сервис \"${service.name}\"? Все его слоты будут удалены!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onServiceDelete(service)
                        serviceToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { serviceToDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showAddSlotSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddSlotSheet = false
            }
        ) {
            AddServiceScreen(
                onSave = { service ->
                    onSave(service)
                    showAddSlotSheet = false
                }
            )
        }
    }
}