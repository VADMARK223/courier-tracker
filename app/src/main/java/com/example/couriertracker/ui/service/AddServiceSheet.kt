package com.example.couriertracker.ui.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun AddServiceSheet(
    onSave: (Service) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(
            start = 24.dp,
            end = 24.dp,
            bottom = 24.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp) // Одинаковые отступы между элементами
    ) {
        Text(
            text = "Новый сервис",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название сервиса") },
            singleLine = true, // Чтобы название не переносилось на много строк при случайном Enter
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onSave(
                    Service(
                        name = name.trim()
                    )
                )
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}