package com.example.couriertracker.ui.service.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.Service

@Composable
fun ServicesList(
    services: List<Service>,
    onServiceDeleteClick: (Service) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (services.isEmpty()) {
            Text(
                text = "Список сервисов пуст",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = services,
                    key = { service -> service.id }
                ) { service ->
                    ServiceListItem(
                        item = service,
                        onDeleteClick = onServiceDeleteClick
                    )
                }
            }
        }
    }
}