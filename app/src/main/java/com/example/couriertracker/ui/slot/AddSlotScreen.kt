package com.example.couriertracker.ui.slot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun AddSlotScreen(
    onSave: (Slot) -> Unit,
    repository: DataRepository,
    settingsRepository: SettingsRepository,
) {
    val viewModel: AddSlotViewModel = viewModel {
        AddSlotViewModel(
            repository = repository,
            settingsRepository = settingsRepository
        )
    }

    val savedServiceId by viewModel.lastSelectedServiceId.collectAsStateWithLifecycle(initialValue = null)
    var selectedServiceId by remember { mutableStateOf<Long?>(null) }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(18, 0)) }


    val services by viewModel.getServices()
        .collectAsStateWithLifecycle(emptyList())


    LaunchedEffect(savedServiceId) {
        if (selectedServiceId == null && savedServiceId != null) {
            selectedServiceId = savedServiceId
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val slotValidation by remember {
        derivedStateOf {
            if (selectedServiceId == null) {
                return@derivedStateOf SlotState(
                    isValid = false,
                    displayText = "",
                    errorText = "Необходимо выбрать сервис"
                )
            }

            val duration = Duration.between(startTime, endTime)

            if (duration.isNegative || duration.isZero) {
                return@derivedStateOf SlotState(
                    isValid = false,
                    displayText = "",
                    errorText = "Период должен быть больше нуля"
                )
            }

            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60

            val text = when {
                hours <= 0L && minutes <= 0L -> ""
                hours == 0L -> "($minutes м.) "
                minutes == 0L -> "($hours ч.) "
                else -> "($hours ч. $minutes м.) "
            }

            SlotState(
                isValid = true,
                displayText = text,
                errorText = null
            )

        }
    }

    Column(
//        modifier = modifier.fillMaxWidth()
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Новый слот ${slotValidation.displayText}",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(services) { service ->
                ServiceItem(
                    item = service,
                    onSelected = {
                        selectedServiceId = service.id
                        viewModel.saveLastSelectedServiceId(service.id)
                    },
                    isSelected = service.id == selectedServiceId,
                )
            }
        }

        Text(text = "Дата:", style = MaterialTheme.typography.labelLarge)
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(date.format(dateFormatter))
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "С:", style = MaterialTheme.typography.labelLarge)
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(startTime.format(timeFormatter))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "До:", style = MaterialTheme.typography.labelLarge)
                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(endTime.format(timeFormatter))
                }
            }
        }

//        Spacer(modifier = Modifier.height(32.dp))

        if (!slotValidation.isValid && slotValidation.errorText != null) {
            Text(
                text = slotValidation.errorText!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            enabled = slotValidation.isValid,
            onClick = {
                val startDateTime = LocalDateTime.of(date, startTime)
                val endDateTime = LocalDateTime.of(date, endTime)

                onSave(
                    Slot(
                        serviceId = selectedServiceId!!,
                        startTime = startDateTime,
                        endTime = endDateTime
                    )
                )

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()
                            }
                            showDatePicker = false
                        }
                    ) { Text("ОК") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showStartTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = startTime.hour,
                initialMinute = startTime.minute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { showStartTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            startTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            showStartTimePicker = false
                        }
                    ) { Text("ОК") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartTimePicker = false }) { Text("Отмена") }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }

        if (showEndTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = endTime.hour,
                initialMinute = endTime.minute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { showEndTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            endTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            showEndTimePicker = false
                        }
                    ) { Text("ОК") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndTimePicker = false }) { Text("Отмена") }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }
    }
}
