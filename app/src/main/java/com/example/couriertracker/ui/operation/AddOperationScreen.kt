package com.example.couriertracker.ui.operation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.DataRepository
import com.example.couriertracker.data.Operation
import com.example.couriertracker.data.OperationType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddOperationScreen(
    onBack: () -> Unit,
    onSave: (Operation) -> Unit,
    modifier: Modifier = Modifier,
    repository: DataRepository
) {
    var operationType by remember {
        mutableStateOf(OperationType.EXPENSE)
    }
    val viewModel: AddOperationViewModel = viewModel { AddOperationViewModel(repository) }
    val categories by viewModel.getRepositories(operationType).collectAsStateWithLifecycle(emptyList())

    var selectedCategoryId by remember {
        mutableStateOf<Long?>(null)
    }

    var amount by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(LocalDate.now())
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }

            Text(
                text = "Новая операция",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                RadioButton(
                    selected = operationType == OperationType.EXPENSE,
                    onClick = {
                        operationType = OperationType.EXPENSE
                        selectedCategoryId = null
                    }
                )

                Text("Расход")
            }

            Spacer(modifier = Modifier.width(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                RadioButton(
                    selected = operationType == OperationType.INCOME,
                    onClick = {
                        operationType = OperationType.INCOME
                        selectedCategoryId = null
                    }
                )

                Text("Доход")
            }
        }


        categories.forEach { category ->
            Row (
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
            label = {
                Text("Сумма")
            },
            suffix = {
                Text("₽")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                }
        ) {

        }

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

                    onSave(operation)
                }
            },
            enabled = amount.isNotBlank() && selectedCategoryId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
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

/*@Preview(showBackground = true)
@Composable
fun AddOperationScreenPreview() {
    CourierTrackerTheme { AddOperationScreen() }
}*/
