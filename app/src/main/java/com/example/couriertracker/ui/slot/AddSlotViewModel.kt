package com.example.couriertracker.ui.slot

import androidx.lifecycle.ViewModel
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.OperationType
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot
import com.example.couriertracker.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class AddSlotViewModel(
    private val repository: DataRepository
): ViewModel() {
    fun getServices(): Flow<List<Service>> {
        return repository.getServices()
    }

    fun getSlots(): Flow<List<Slot>> {
        return repository.getSlots()
    }
}