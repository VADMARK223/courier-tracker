package com.example.couriertracker.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository
import kotlinx.coroutines.launch

class ServicesViewModel(
    private val repository: DataRepository,
) : ViewModel() {
    val services = repository.getServices()

    fun saveService(service: Service) {
        viewModelScope.launch {
            repository.insertService(service)
        }
    }

    fun deleteService(service: Service) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }
}

