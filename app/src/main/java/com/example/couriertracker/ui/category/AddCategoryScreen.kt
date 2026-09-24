package com.example.couriertracker.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.couriertracker.data.Category
import com.example.couriertracker.data.OperationType

@Composable
fun AddCategoryScreen(
    onBack: () -> Unit,
    onSave: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember {
        mutableStateOf("")
    }

    var categoryType by remember {
        mutableStateOf(OperationType.EXPENSE)
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
                text = "Новая категория",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                RadioButton(
                    selected = categoryType == OperationType.EXPENSE,
                    onClick = {
                        categoryType = OperationType.EXPENSE
                    }
                )

                Text("Расход")
            }

            Spacer(modifier = Modifier.width(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                RadioButton(
                    selected = categoryType == OperationType.INCOME,
                    onClick = {
                        categoryType = OperationType.INCOME
                    }
                )

                Text("Доход")
            }
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
                    Category(
                        name = name.trim(),
                        type = categoryType
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