package com.example.couriertracker.ui.service.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.ui.common.state.AddItemState
import com.example.couriertracker.ui.components.AddItemWarning

@Composable
fun AddServiceSheet(
    onSave: (Service) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    val addState = if (name.isNotBlank()) {
        AddItemState(isValid = true)
    } else {
        AddItemState(
            isValid = false,
            errorText = "Имя сервиса / счета пустое"
        )
    }

    Column(
        modifier = Modifier.padding(
            start = 24.dp,
            end = 24.dp,
            bottom = 24.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp) // Одинаковые отступы между элементами
    ) {
        Text(
            text = "Добавление сервиса / счета",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Введите имя сервиса / счета") },
            singleLine = true, // Чтобы название не переносилось на много строк при случайном Enter
            modifier = Modifier.fillMaxWidth()
        )

        AddItemWarning(addState)

        Button(
            onClick = { onSave(Service(name = name.trim())) },
            enabled = addState.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить")
        }
    }
}