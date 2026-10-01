package com.example.couriertracker.ui.slot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.couriertracker.AddService
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddSlotScreenOld(
    onBack: () -> Unit,

    onServiceDelete: (Service) -> Unit,
    onSlotDelete: (Slot) -> Unit,
    onItemClick: (NavKey) -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository,
    modifier: Modifier = Modifier
) {




    Column(
        modifier = modifier.fillMaxWidth()
    ) {




//        var serviceToDelete by remember { mutableStateOf<Service?>(null) }
//
//
//        var servicesListHint = "Список сервисов"
//        if (services.isEmpty()) {
//            servicesListHint += " пуст"
//        }
//        Text(servicesListHint)



        Button(
            onClick = {
                onItemClick(AddService)
            },
        ) {
            Text("Добавить сервис")
        }

        /*serviceToDelete?.let { service ->
            AlertDialog(
                onDismissRequest = { serviceToDelete = null }, // Закрываем при клике мимо
                title = { Text(text = "Удаление сервиса") },
                text = { Text(text = "Вы уверены, что хотите удалить сервис \"${service.name}\"? Все его слоты будут удалены!") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onServiceDelete(service)
                            serviceToDelete = null
                        }
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { serviceToDelete = null }) {
                        Text("Отмена")
                    }
                }
            )
        }*/

        HorizontalDivider()





    }
}
