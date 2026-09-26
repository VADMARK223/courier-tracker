package com.example.couriertracker.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.Operation
import com.example.couriertracker.data.model.OperationType
import com.example.couriertracker.data.model.OperationWithCategory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.category?.name ?: "Без категории"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = formatMoney(amount), style = MaterialTheme.typography.bodyLarge)

                IconButton(
                    onClick = {
                        onDelete(operation)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить операцию"
                    )
                }
            }
        }
    }

    Text(
        text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
        style = MaterialTheme.typography.bodySmall
    )
}