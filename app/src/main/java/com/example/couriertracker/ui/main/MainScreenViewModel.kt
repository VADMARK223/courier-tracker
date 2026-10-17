package com.example.couriertracker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.ui.main.MainScreenUiState.Success
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: DataRepository
) : ViewModel() {
    private val retryTrigger = MutableStateFlow(0)
    private val _events = MutableSharedFlow<MainScreenEvent>()
    val events = _events.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MainScreenUiState> =
        retryTrigger
            .flatMapLatest {
                repository.operationsWithCategory
                    .map<List<OperationWithCategory>, MainScreenUiState>(::Success)
                    /*.map<List<OperationWithCategory>, MainScreenUiState> {
                        delay(2000.milliseconds)
                        throw RuntimeException("Тестовая ошибка")
                    }*/
                    .onStart { emit(MainScreenUiState.Loading) }
                    .catch { emit(MainScreenUiState.Error(it)) }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MainScreenUiState.Loading
            )

    fun retry() {
        retryTrigger.value++
    }

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch {
            try {
                repository.deleteOperation(operation)
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _events.emit(MainScreenEvent.DeleteFailed)
            }
        }
    }
}

sealed interface MainScreenEvent {
    data object DeleteFailed : MainScreenEvent
}


sealed interface MainScreenUiState {
    object Loading : MainScreenUiState

    data class Error(val throwable: Throwable) : MainScreenUiState

    data class Success(val data: List<OperationWithCategory>) : MainScreenUiState
}
