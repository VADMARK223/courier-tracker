package com.example.couriertracker.ui.slot.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import kotlinx.coroutines.flow.Flow

@Composable
fun SlotsList(
    slotsWithService: List<SlotWithService>,
    onSlotDelete: (Slot) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
    ) {
        if (slotsWithService.isEmpty()) {
            Text(
                text = "Список слотов пуст",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = slotsWithService,
                    key = { item -> item.slot.id }
                ) { item ->
                    SlotListItem(
                        item = item,
                        onDelete = { slot -> onSlotDelete(slot) }
                    )
                }
            }
        }
    }


    /*val slotWithService by viewModel.getSlotsWithService()
        .collectAsStateWithLifecycle(emptyList())

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    )
    {
        items(slotWithService) { item ->
            SlotListItem(
                item = item,
                onDelete = { item ->
                    onSlotDelete(item)
                }
            )
        }
    }*/
}