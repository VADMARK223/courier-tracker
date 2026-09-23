package com.example.couriertracker.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.couriertracker.AddCategory
import com.example.couriertracker.AddOperation
import com.example.couriertracker.data.DataRepository
import com.example.couriertracker.data.OperationType
import com.example.couriertracker.data.OperationWithCategory

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    repository: DataRepository,
) {

    val viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(repository) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        MainScreenUiState.Loading -> {
            // Blank
        }

        is MainScreenUiState.Success -> {
            MainScreen(
                operations = (state as MainScreenUiState.Success).data,
                onItemClick = onItemClick,
            )
        }

        is MainScreenUiState.Error -> {
            Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
        }
    }
}

@Composable
internal fun MainScreen(
    operations: List<OperationWithCategory>,
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {

    val balance: Long = operations.sumOf { item ->
        if (item.operation.type == OperationType.INCOME) {
            item.operation.amount
        } else {
            -item.operation.amount
        }
    }


    Column(modifier) {
        Text("Баланс: ${formatMoney(balance)}")

        HorizontalDivider()

        operations.forEach { item ->
            Text(
                text = "${item.category?.name ?: "Без категории"}: " + formatMoney(item.operation.amount)
            )
        }

        Button(
            onClick = { onItemClick(AddCategory) },
        ) {
            Text("Добавить категорию")
        }

        Button(
            onClick = { onItemClick(AddOperation) },
        ) {
            Text("Добавить операцию")
        }
    }
}

fun formatMoney(amount: Long): String {
    val rubles = amount / 100
    val kopecks = kotlin.math.abs(amount % 100)

    return "$rubles,${kopecks.toString().padStart(2, '0')} ₽"
}
