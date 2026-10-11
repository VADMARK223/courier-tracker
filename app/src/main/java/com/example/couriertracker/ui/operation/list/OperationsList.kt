package com.example.couriertracker.ui.operation.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationWithCategory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OperationsList(
    operations: List<OperationWithCategory>,
    onOperationDelete: (Operation) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (operations.isEmpty()) {
            Text(
                text = "Список операций пуст",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = operations,
                    key = { item -> item.operation.id }
                ) { item ->
                    OperationItem(
                        item = item,
                        onDelete = { slot -> onOperationDelete(slot) }
                    )
                }
            }
        }
    }
}