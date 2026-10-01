package com.example.couriertracker.ui.service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
                onServiceDelete = onServiceDelete,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )

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
            /*AddSlotScreen(
                onSave = { slot ->
                    onSave(slot)
                    showAddSlotSheet = false
                },
                repository = repository,
                settingsRepository = settingsRepository
            )*/
        }
    }
}