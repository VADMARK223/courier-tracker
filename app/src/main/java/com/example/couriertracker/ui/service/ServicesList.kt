package com.example.couriertracker.ui.service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.slot.AddSlotViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServicesList(
    repository: DataRepository,
    settingsRepository: SettingsRepository,
    onServiceDelete: (Service) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AddSlotViewModel = viewModel {
        AddSlotViewModel(
            repository = repository,
            settingsRepository = settingsRepository
        )
    }

    val services by viewModel.getServices()
        .collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        services.forEach { service ->
            ServiceListItem(
                item = service,
                onDelete = { service ->
                    onServiceDelete(service)
                },
            )
        }
    }
}