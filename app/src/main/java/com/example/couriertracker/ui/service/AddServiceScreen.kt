package com.example.couriertracker.ui.service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.Service

@Composable
fun AddServiceScreen(
    onSave: (Service) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember {
        mutableStateOf("")
    }

    Column(
//        modifier = modifier.fillMaxWidth()
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

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
                Text("Название сервиса")
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