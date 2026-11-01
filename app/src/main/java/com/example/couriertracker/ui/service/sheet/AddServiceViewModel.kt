package com.example.couriertracker.ui.service.sheet

import androidx.lifecycle.ViewModel
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository

class AddServiceViewModel(
    private val repository: DataRepository
) : ViewModel() {
    suspend fun saveService(service: Service) {
        repository.insertService(service)
    }
}