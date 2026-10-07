package com.example.couriertracker.ui.slot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.slot.SlotsScreenUiState.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SlotsScreenViewModel(
    private val repository: DataRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SlotsScreenUiState> =
        retryTrigger
            .flatMapLatest {
                repository.slotWithService
                    .map<List<SlotWithService>, SlotsScreenUiState>(::Success)
                    .onStart {
                        emit(SlotsScreenUiState.Loading)
                    }
                    .catch { throwable ->
                        emit(SlotsScreenUiState.Error(throwable))
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SlotsScreenUiState.Loading
            )

    fun retry() {
        retryTrigger.value++
    }

    /*@OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SlotsScreenUiState> =
        retryTrigger
            .flatMapLatest {
                repository.slotWithService
                    .map<List<SlotWithService>, SlotsScreenUiState> {
                        delay(2000.milliseconds)
                        throw RuntimeException("Тестовая ошибка")
                    }
                    .onStart {
                        emit(SlotsScreenUiState.Loading)
                    }
                    .catch { throwable ->
                        emit(SlotsScreenUiState.Error(throwable))
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                SlotsScreenUiState.Loading
            )*/

    fun getServices(): Flow<List<Service>> {
        return repository.getServices()
    }

    fun saveSlot(slot: Slot) {
        viewModelScope.launch {
            repository.insertSlot(slot)
        }
    }

    fun deleteSlot(slot: Slot) {
        viewModelScope.launch {
            repository.deleteSlot(slot)
        }
    }

    val lastSelectedServiceId: Flow<Long?> = settingsRepository.lastSelectedServiceId

    fun saveLastSelectedServiceId(serviceId: Long) {
        viewModelScope.launch {
            settingsRepository.saveLastSelectedServiceId(serviceId)
        }
    }
}

sealed interface SlotsScreenUiState {
    object Loading : SlotsScreenUiState

    data class Error(val throwable: Throwable) : SlotsScreenUiState

    data class Success(val data: List<SlotWithService>) : SlotsScreenUiState
}