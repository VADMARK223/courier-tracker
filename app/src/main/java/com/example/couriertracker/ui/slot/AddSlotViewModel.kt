package com.example.couriertracker.ui.slot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddSlotViewModel(
    private val repository: DataRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    fun getServices(): Flow<List<Service>> {
        return repository.getServices()
    }

    fun getSlots(): Flow<List<Slot>> {
        return repository.getSlots()
    }

    val lastSelectedServiceId: Flow<Long?> = settingsRepository.lastSelectedServiceId

    fun saveLastSelectedServiceId(serviceId: Long) {
        viewModelScope.launch {
            settingsRepository.saveLastSelectedServiceId(serviceId)
        }
    }
}