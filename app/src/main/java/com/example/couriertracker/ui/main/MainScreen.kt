package com.example.couriertracker.ui.main


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.repository.DataRepository

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    repository: DataRepository,
) {
    val viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(repository) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) { // Сохраняем в локальную переменную для стабильности Smart Cast
        MainScreenUiState.Loading -> {
            // Blank
        }

        is MainScreenUiState.Success -> {
            MainScreen(
                operations = currentState.data,
                onDeleteOperation = viewModel::deleteOperation
            )
        }

        is MainScreenUiState.Error -> {
            Text("Error loading data: ${currentState.throwable.message}")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MainScreen(
    operations: List<OperationWithCategory>,
    onDeleteOperation: (Operation) -> Unit,
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

        LazyColumn {
            items(operations) { item ->
                OperationItem(item = item, onDelete = onDeleteOperation)
            }
        }
    }
}

fun formatMoney(amount: Long): String {
    val rubles = amount / 100
    val kopecks = kotlin.math.abs(amount % 100)

    return "$rubles,${kopecks.toString().padStart(2, '0')} ₽"
}
