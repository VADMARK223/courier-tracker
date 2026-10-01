package com.example.couriertracker.ui.slot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.AddService
import com.example.couriertracker.data.model.Slot
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SlotsScreen(
    onBack: () -> Unit,
//                onSave: (Slot) -> Unit,
//                onServiceDelete: (Service) -> Unit,
    onSlotDelete: (Slot) -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {
    val viewModel: AddSlotViewModel = viewModel {
        AddSlotViewModel(
            repository = repository,
            settingsRepository = settingsRepository
        )
    }

    val savedServiceId by viewModel.lastSelectedServiceId.collectAsStateWithLifecycle(initialValue = null)
    var selectedServiceId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(savedServiceId) {
        if (selectedServiceId == null && savedServiceId != null) {
            selectedServiceId = savedServiceId
        }
    }

    val slots by viewModel.getSlots()
        .collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
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
                text = "Список слотов",
                style = MaterialTheme.typography.headlineMedium
            )
        }


        slots.forEach { slot ->
            SlotItem(
                item = slot,
                onDelete = { slot ->
                    onSlotDelete(slot)
                }
            )
        }
    }
}