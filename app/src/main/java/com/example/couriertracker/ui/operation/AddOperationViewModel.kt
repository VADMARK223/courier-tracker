package com.example.couriertracker.ui.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.model.OperationType
import com.example.couriertracker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddOperationViewModel(
    private val repository: DataRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val lastOperationType: Flow<OperationType> = settingsRepository.lastOperationType

    fun saveLastOperationType(type: OperationType) {
        viewModelScope.launch {
            settingsRepository.saveLastOperationType(type)
        }
    }


    fun getRepositories(type: OperationType): Flow<List<Category>> {
        return repository.getCategoriesByType(type)
    }
}