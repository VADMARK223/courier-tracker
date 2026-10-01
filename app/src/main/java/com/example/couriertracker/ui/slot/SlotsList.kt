package com.example.couriertracker.ui.slot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.model.Slot
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository

@Composable
fun SlotsList(
    repository: DataRepository,
    settingsRepository: SettingsRepository,
    onSlotDelete: (Slot) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AddSlotViewModel = viewModel {
        AddSlotViewModel(
            repository = repository,
            settingsRepository = settingsRepository
        )
    }

    val slots by viewModel.getSlots()
        .collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

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