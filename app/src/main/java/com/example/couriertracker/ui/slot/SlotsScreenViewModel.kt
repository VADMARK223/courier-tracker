package com.example.couriertracker.ui.slot

import androidx.lifecycle.ViewModel
import com.example.couriertracker.data.model.slot.SlotWithService
import com.example.couriertracker.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class SlotsScreenViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    fun getSlotsWithService(): Flow<List<SlotWithService>> {
        return repository.slotWithService
    }
}