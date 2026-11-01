package com.example.couriertracker.ui.service.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.util.toKopecksOrNull

@Composable
fun AddServiceSheet(
    onSave: (Service) -> Unit,
    isSaving: Boolean = false
) {
    var balance by remember { mutableStateOf("0") }
    var name by remember { mutableStateOf("") }
    var isTouched by remember { mutableStateOf(false) }

    val isValid = name.isNotBlank()
    val showError = isTouched && !isValid

    val balanceInKopecks = if (balance.isBlank()) {
        0L
    } else {
        balance.toKopecksOrNull()
    }

    val isBalanceValid = balanceInKopecks != null

    Column(
        modifier = Modifier.padding(
            start = 24.dp,
            end = 24.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Добавление счета",
            style = MaterialTheme.typography.headlineMedium
        )


        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("Начальный баланс") },
            placeholder = { Text("0") },
            suffix = { Text("₽") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            isError = !isBalanceValid,
            supportingText = {
                Text(
                    if (!isBalanceValid) "Некорректная сумма"
                    else "\u00A0"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                isTouched = true
            },
            label = {
                Text("Введите имя счета")
            },
            singleLine = true,
            isError = showError,
            supportingText = {
                Text(
                    text = if (showError) {
                        "Название не может быть пустым"
                    } else {
                        "\u00A0"
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onSave(Service(balance = balanceInKopecks ?: 0L, name = name.trim()))
            },
            enabled = isValid && !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Сохранение..." else "Сохранить")
        }
    }
}