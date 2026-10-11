package com.example.couriertracker.ui.category.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.ui.category.CategoriesScreenViewModel

@Composable
fun AddCategorySheet(viewModel: CategoriesScreenViewModel, onSaved: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var categoryType by remember { mutableStateOf(OperationType.EXPENSE) }

    Column(modifier = Modifier.padding(horizontal = 25.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Новая категория",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = categoryType == OperationType.EXPENSE,
                onClick = {
                    categoryType = OperationType.EXPENSE
                }
            )

            Text("Расход")

            RadioButton(
                selected = categoryType == OperationType.INCOME,
                onClick = {
                    categoryType = OperationType.INCOME
                }
            )

            Text("Доход")
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
                viewModel.saveCategory(
                    Category(
                        name = name.trim(),
                        type = categoryType
                    )
                )

                onSaved() // TODO: доделать нормально
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
 }

