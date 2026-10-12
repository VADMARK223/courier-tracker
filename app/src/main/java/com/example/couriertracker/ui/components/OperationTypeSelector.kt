package com.example.couriertracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.model.operation.OperationType

@Composable
fun OperationTypeSelector(
    selectedType: OperationType,
    onTypeSelected: (OperationType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(
            32.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OperationType.entries.forEach { type ->
            Row(
                modifier = Modifier.selectable(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedType == type,
                    onClick = null
                )

                Text(
                    text = when (type) {
                        OperationType.EXPENSE -> "Расходы"
                        OperationType.INCOME -> "Доходы"
                    }
                )

                OperationTypeIcon(type == OperationType.INCOME)
            }
        }
    }
}

