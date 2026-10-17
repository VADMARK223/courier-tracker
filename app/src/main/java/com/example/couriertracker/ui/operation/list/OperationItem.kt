package com.example.couriertracker.ui.operation.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.ui.components.OperationTypeIcon
import com.example.couriertracker.ui.main.formatMoney
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun OperationItem(
    item: OperationWithCategory,
    onDelete: (Operation) -> Unit,
    modifier: Modifier = Modifier
) {
    val operation = item.operation

    val amount = if (operation.type == OperationType.INCOME) {
        operation.amount
    } else {
        -operation.amount
    }

    val date = LocalDate.ofEpochDay(operation.date)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.category?.name ?: "Без категории"
                )
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = formatMoney(amount), style = MaterialTheme.typography.bodyLarge)

                OperationTypeIcon(operation.type == OperationType.INCOME)

                IconButton(onClick = { onDelete(operation) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить операцию",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
    }
}