package com.example.couriertracker.ui.slot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.Slot
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
    onBack: () -> Unit,
    onSave: (Slot) -> Unit, // Предполагается, что Slot принимает LocalDateTime/LocalTime или String
    modifier: Modifier = Modifier
) {
    // Состояния для даты и времени
    var date by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(18, 0)) }

    // Состояния видимости диалогов
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Шапка с кнопкой назад
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            Text(
                text = "Новый слот",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка выбора даты
        Text(text = "Дата:", style = MaterialTheme.typography.labelLarge)
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(date.format(dateFormatter))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор временного интервала (Рядом друг с другом)
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

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка Сохранить
        Button(
            onClick = {
                // Комбинируем дату и время (если объект Slot требует LocalDateTime)
                val startDateTime = LocalDateTime.of(date, startTime)
                val endDateTime = LocalDateTime.of(date, endTime)

                // Передайте данные в соответствии с вашей структурой Slot
                // Например: onSave(Slot(start = startDateTime, end = endDateTime))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        // --- ДИАЛОГ ВЫБОРА ДАТЫ ---
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

        // --- ДИАЛОГ ВЫБОРА ВРЕМЕНИ НАЧАЛА ---
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

        // --- ДИАЛОГ ВЫБОРА ВРЕМЕНИ ОКОНЧАНИЯ ---
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
