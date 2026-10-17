package com.example.couriertracker.ui.main

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.ui.components.ErrorBox
import com.example.couriertracker.ui.components.Preloader
import com.example.couriertracker.ui.operation.list.OperationItem

@Composable
fun MainScreen(repository: DataRepository, onMenuClick: () -> Unit) {
    val viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(repository) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                MainScreenEvent.DeleteFailed ->
                    snackbarHostState.showSnackbar("Не удалось удалить операцию")
            }
        }
    }

    when (val currentState = state) {
        MainScreenUiState.Loading -> {
            Preloader()
        }

        is MainScreenUiState.Success -> {
            MainScreenContent(
                operations = currentState.data,
                onDeleteOperation = viewModel::deleteOperation,
                onMenuClick = onMenuClick,
                snackbarHostState = snackbarHostState
            )
        }

        is MainScreenUiState.Error -> {
            ErrorBox(
                "Не удалось загрузить операции",
                currentState.throwable.message,
                viewModel::retry
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreenContent(
    operations: List<OperationWithCategory>,
    onDeleteOperation: (Operation) -> Unit,
    onMenuClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {

    // TODO: Long переделать в BigInteger
    val balance: Long = operations.sumOf { item ->
        if (item.operation.type == OperationType.INCOME) {
            item.operation.amount
        } else {
            -item.operation.amount
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                items(
                    items = operations,
                    key = { it.operation.id }
                ) { item ->
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
