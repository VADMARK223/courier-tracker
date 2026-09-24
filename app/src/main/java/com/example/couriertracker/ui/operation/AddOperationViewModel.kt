package com.example.couriertracker.ui.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.Category
import com.example.couriertracker.data.DataRepository
import com.example.couriertracker.data.OperationType
import com.example.couriertracker.data.SettingsRepository
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