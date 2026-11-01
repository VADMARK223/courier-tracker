package com.example.couriertracker.ui.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OperationsScreenViewModel(
    private val repository: DataRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val serviceRepository = repository.getServices()
    private val categoryRepository = repository.getCategories()

    val uiState: StateFlow<OperationsUiState> = combine(
        serviceRepository,
        categoryRepository
    ) { services, categories ->
        OperationsUiState(
            hasServices = services.isNotEmpty(),
            hasCategories = categories.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = OperationsUiState()
    )

    val lastOperationType: Flow<OperationType> = settingsRepository.lastOperationType

    fun saveLastOperationType(type: OperationType) {
        viewModelScope.launch {
            settingsRepository.saveLastOperationType(type)
        }
    }

    fun getServices(): Flow<List<Service>> {
        return repository.getServices()
    }


    fun getRepositories(type: OperationType): Flow<List<Category>> {
        return repository.getCategoriesByType(type)
    }

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch {
            repository.deleteOperation(operation)
        }
    }

    fun saveOperation(operation: Operation) {
        viewModelScope.launch {
            repository.addOperation(operation)
        }
    }

    fun saveService(service: Service) {
        viewModelScope.launch {
            repository.insertService(service)
        }
    }
}