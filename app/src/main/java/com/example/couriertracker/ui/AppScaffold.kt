package com.example.couriertracker.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.example.couriertracker.Categories
import com.example.couriertracker.Main
import com.example.couriertracker.Operations
import com.example.couriertracker.Services
import com.example.couriertracker.Slots

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    currentScreen: NavKey,
    onMainClick: () -> Unit,
    onServiceClick: () -> Unit,
    onSlotClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onOperationClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Main,
                    onClick = onMainClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = "Главная"
                        )
                    },
                    label = { Text("Главная") }
                )

                NavigationBarItem(
                    selected = currentScreen == Services,
                    onClick = onServiceClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Storefront,
                            contentDescription = "Сервисы"
                        )
                    },
                    label = { Text("Сервисы") }
                )

                NavigationBarItem(
                    selected = currentScreen == Categories,
                    onClick = onCategoryClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Category,
                            contentDescription = "Категории"
                        )
                    },
                    label = { Text("Категории") }
                )

                NavigationBarItem(
                    selected = currentScreen == Operations,
                    onClick = onOperationClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = "Операции"
                        )
                    },
                    label = { Text("Операции") }
                )

                NavigationBarItem(
                    selected = currentScreen == Slots,
                    onClick = onSlotClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = "Слоты"
                        )
                    },
                    label = { Text("Слоты") }
                )
            }
        },
        content = content
    )
}