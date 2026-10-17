package com.example.couriertracker.ui.operation.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.ui.common.state.AddItemState
import com.example.couriertracker.ui.components.AddItemWarning
import com.example.couriertracker.ui.components.OperationTypeSelector
import com.example.couriertracker.ui.operation.OperationsScreenViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOperationSheet(
    viewModel: OperationsScreenViewModel,
    onSaved: () -> Unit,
) {
    val operationType by viewModel.lastOperationType.collectAsStateWithLifecycle(initialValue = OperationType.EXPENSE)

    val categories by viewModel.getRepositories(operationType)
        .collectAsStateWithLifecycle(emptyList())

    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    var amount by remember { mutableStateOf("") }

    var date by remember { mutableStateOf(LocalDate.now()) }

    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Добавление операции",
            style = MaterialTheme.typography.headlineMedium
        )

        OperationTypeSelector(selectedType = operationType, onTypeSelected = { type ->
            viewModel.saveLastOperationType(type)
            selectedCategoryId = null
        })

        HorizontalDivider()

        Text(
            text = "Категории:",
            style = MaterialTheme.typography.bodyMedium
        )

        categories.forEach { category ->
            Row(
                modifier = Modifier.clickable {
                    selectedCategoryId = category.id
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedCategoryId == category.id,
                    onClick = {
                        selectedCategoryId = category.id
                    }
                )

                Text(category.name)
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    amount = newValue
                }
            },
            label = { Text("Сумма") },
            suffix = { Text("₽") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = {
                showDatePicker = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                date.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
                )
            )
        }

        val addState = when {
            amount.isBlank() -> AddItemState(
                isValid = false,
                errorText = "Введите сумму"
            )

            selectedCategoryId == null -> AddItemState(
                isValid = false,
                errorText = "Выберите категорию"
            )

            else -> AddItemState(isValid = true)
        }

        AddItemWarning(addState)

        Button(
            onClick = {
                val amountInRubles = amount.toLongOrNull()

                if (amountInRubles != null) {
                    val operation = Operation(
                        id = 0,
                        type = operationType,
                        amount = amountInRubles * 100,
                        categoryId = selectedCategoryId,
                        date = date.toEpochDay()
                    )

                    viewModel.saveOperation(operation)

                    onSaved()
                }
            },
            enabled = addState.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить")
        }
    }


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            date = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}