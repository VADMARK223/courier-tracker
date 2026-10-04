package com.example.couriertracker.ui.service.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.couriertracker.data.model.Service

@Composable
fun DeleteServiceDialog(
    service: Service,
    onConfirm: (Service) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Удаление сервиса") },
        text = { Text(text = "Удалить сервис «${service.name}»?\nВсе связанные с ним слоты также будут удалены.") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(service)
                }
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}