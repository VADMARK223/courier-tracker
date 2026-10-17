package com.example.couriertracker.ui.slot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.couriertracker.data.model.slot.SlotWithService
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.components.ErrorBox
import com.example.couriertracker.ui.components.Preloader
import com.example.couriertracker.ui.slot.list.SlotsList
import com.example.couriertracker.ui.slot.sheet.AddSlotSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotsScreen(
    onBack: () -> Unit,
    onAddServiceClick: () -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository,
) {
    val viewModel: SlotsScreenViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                SlotsScreenViewModel(repository, settingsRepository)
            }
        }
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        SlotsScreenUiState.Loading -> {
            Preloader()
        }

        is SlotsScreenUiState.Success -> {
            SlotsScreenContent(
                slotsWithService = currentState.data,
                onBack = onBack,
                onAddServiceClick = onAddServiceClick,
                viewModel = viewModel,
            )
        }

        is SlotsScreenUiState.Error -> {
            ErrorBox(
                "Не удалось загрузить слоты",
                currentState.throwable.message,
                viewModel::retry
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SlotsScreenContent(
    slotsWithService: List<SlotWithService>,
    onBack: () -> Unit,
    onAddServiceClick: () -> Unit,
    viewModel: SlotsScreenViewModel,
) {
    var showAddSlotSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Слоты")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddSlotSheet = true
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить слот"
                )
            }
        },
        content = { paddingValues ->
            SlotsList(
                slotsWithService = slotsWithService,
                onSlotDelete = { slot -> viewModel.deleteSlot(slot) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )

    if (showAddSlotSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.resetSaveState()
                showAddSlotSheet = false
            }
        ) {
            AddSlotSheet(
                viewModel = viewModel,
                onSaved = { showAddSlotSheet = false },
                onAddServiceClick = {
                    showAddSlotSheet = false
                    onAddServiceClick()
                },
            )
        }
    }
}