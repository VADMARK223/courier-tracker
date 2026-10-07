package com.example.couriertracker.ui.main


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
    onMenuClick: () -> Unit,
) {
    val viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(repository) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val currentState =
        state) { // Сохраняем в локальную переменную для стабильности Smart Cast
        MainScreenUiState.Loading -> {
            // Blank
        }

        is MainScreenUiState.Success -> {
            MainScreenContent(
                operations = currentState.data,
                onDeleteOperation = viewModel::deleteOperation,
                onMenuClick = onMenuClick
            )
        }

        is MainScreenUiState.Error -> {
            Text("Error loading data: ${currentState.throwable.message}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MainScreenContent(
    operations: List<OperationWithCategory>,
    onDeleteOperation: (Operation) -> Unit,
    onMenuClick: () -> Unit
) {

    val balance: Long = operations.sumOf { item ->
        if (item.operation.type == OperationType.INCOME) {
            item.operation.amount
        } else {
            -item.operation.amount
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Courier Tracker")
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Открыть меню"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Баланс: ${formatMoney(balance)}")

            HorizontalDivider()

            LazyColumn {
                items(operations) { item ->
                    OperationItem(item = item, onDelete = onDeleteOperation)
                }
            }
        }
    }
}

fun formatMoney(amount: Long): String {
    val rubles = amount / 100
    val kopecks = kotlin.math.abs(amount % 100)

    return "$rubles,${kopecks.toString().padStart(2, '0')} ₽"
}
