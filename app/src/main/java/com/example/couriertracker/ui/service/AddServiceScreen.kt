package com.example.couriertracker.ui.service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.Service

@Composable
fun AddServiceScreen(
    onBack: () -> Unit,
    onSave: (Service) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember {
        mutableStateOf("")
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
                text = "Новый сервис",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Название категории")
            },
            modifier = Modifier.fillMaxWidth()
        )

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