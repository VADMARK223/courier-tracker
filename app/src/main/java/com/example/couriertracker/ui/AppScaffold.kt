package com.example.couriertracker.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.example.couriertracker.AddCategory
import com.example.couriertracker.AddOperation
import com.example.couriertracker.Main
import com.example.couriertracker.Slots

@Composable
fun AppScaffold(
    currentScreen: NavKey,
    onMainClick: () -> Unit,
    onServiceClick: () -> Unit,
    onSlotClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onOperationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
//        contentWindowInsets = WindowInsets.navigationBars,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Main,
                    onClick = onMainClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Главная"
                        )
                    },
                    label = { Text("Главная") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onServiceClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Report,
                            contentDescription = "Сервисы"
                        )
                    },
                    label = { Text("Сервисы") }
                )

                NavigationBarItem(
                    selected = currentScreen == Slots,
                    onClick = onSlotClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = "Слоты"
                        )
                    },
                    label = { Text("Слоты") }
                )

                NavigationBarItem(
                    selected = currentScreen == AddOperation,
                    onClick = onOperationClick,
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = "Операции"
                        )
                    },
                    label = { Text("Операции") }
                )



                NavigationBarItem(
                    selected = currentScreen == AddCategory,
                    onClick = onCategoryClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Категории"
                        )
                    },
                    label = { Text("Категории") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onSettingsClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Настройки"
                        )
                    },
                    label = { Text("Настройки") }
                )

            }
        },
        content = content
    )
}