package com.example.couriertracker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.ui.main.MainScreenUiState.Success
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    val uiState: StateFlow<MainScreenUiState> =
        dataRepository.operationsWithCategory
            .map<List<OperationWithCategory>, MainScreenUiState>(::Success) // Заворачивает успешный список операций в состояние Success.
            .catch { emit(MainScreenUiState.Error(it)) } // Перехватывает ошибки (например, сбой БД) и передводит экран в состояние Error.
            .stateIn( // Конвертирует обычный Flow в StateFlow (горячий поток), который кэширует последнее значение.
                viewModelScope,
                SharingStarted.WhileSubscribed(5000), // Если пользователь свернет приложение, поток будет удерживать данные еще 5 секунд, прежде чем отписать от базы данных, это экономит ресурсы.
                MainScreenUiState.Loading // Начальное состояние экрана, пока данные еще грузятся.
            )

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch {
            dataRepository.deleteOperation(operation)
        }
    }
}


sealed interface MainScreenUiState {
    object Loading : MainScreenUiState

    data class Error(val throwable: Throwable) : MainScreenUiState

    data class Success(val data: List<OperationWithCategory>) : MainScreenUiState
}
