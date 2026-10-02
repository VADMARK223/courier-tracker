package com.example.couriertracker.ui.slot

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository

@Composable
fun SlotsList(
    repository: DataRepository,
    onSlotDelete: (Slot) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SlotsScreenViewModel = viewModel {
        SlotsScreenViewModel(
            repository = repository
        )
    }

    val slotWithService by viewModel.getSlotsWithService()
        .collectAsStateWithLifecycle(emptyList())

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    )
    {
        items(slotWithService) { item ->
            SlotItem(
                item = item,
                onDelete = { item ->
                    onSlotDelete(item)
                }
            )
        }
    }

    /*Column(
        modifier = modifier.fillMaxWidth()
    ) {
        slotWithService.forEach { item ->
            SlotItem(
                item = item,
                onDelete = { item ->
                    onSlotDelete(item)
                }
            )
        }
    }*/
}