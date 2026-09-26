package com.example.couriertracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.room3.Room
import com.example.couriertracker.data.database.AppDatabase
import com.example.couriertracker.data.repository.DefaultDataRepository
import com.example.couriertracker.data.database.MIGRATION_1_2
import com.example.couriertracker.data.database.MIGRATION_2_3
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.AppScaffold
import com.example.couriertracker.ui.AppScreen
import com.example.couriertracker.ui.category.AddCategoryScreen
import com.example.couriertracker.ui.main.MainScreen
import com.example.couriertracker.ui.operation.AddOperationScreen
import com.example.couriertracker.ui.service.AddServiceScreen
import com.example.couriertracker.ui.slot.AddSlotScreen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val database = remember {
        Room.databaseBuilder<AppDatabase>(
            context = applicationContext,
            name = "courier_tacker.db"
        )
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    val repository = remember {
        DefaultDataRepository(
            database.serviceDao(),
            database.slotDao(),
            database.categoryDao(),
            database.operationDao())
    }

    val settingsRepository = remember {
        SettingsRepository(applicationContext)
    }

    val backStack = rememberNavBackStack(Main)

    AppScaffold(
        currentScreen = backStack.last(),
        onMainClick = {
            backStack.clear()
            backStack.add(Main)
        },
        onCategoryClick = {
            backStack.add(AddCategory)
        },
        onOperationClick = {
            backStack.add(AddOperation)
        },
        onSettingsClick = {}
    ) { paddingValues ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(paddingValues),
            entryProvider =
                entryProvider {
                    entry<Main> {
                        AppScreen {
                            MainScreen(
                                onItemClick = { navKey -> backStack.add(navKey) },
                                repository = repository,
                            )
                        }

                    }
                    entry<AddService> {
                        AppScreen {
                            AddServiceScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onSave = { service ->
                                    scope.launch {
                                        repository.addService(service)
                                        backStack.removeLastOrNull()
                                    }
                                }
                            )
                        }
                    }
                    entry<AddSlot> {
                        AppScreen {
                            AddSlotScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onSave = { slot ->
                                    scope.launch {
                                        repository.addSlot(slot)
                                        backStack.removeLastOrNull()
                                    }
                                }
                            )
                        }
                    }
                    entry<AddCategory> {
                        AppScreen {
                            AddCategoryScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onSave = { category ->
                                    scope.launch {
                                        repository.addCategory(category)
                                        backStack.removeLastOrNull()
                                    }
                                }
                            )
                        }
                    }
                    entry<AddOperation> {
                        AppScreen {
                            AddOperationScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onSave = { operation ->
                                    scope.launch {
                                        repository.addOperation(operation)
                                        backStack.removeLastOrNull()
                                    }
                                },
                                repository = repository,
                                settingsRepository = settingsRepository
                            )
                        }
                    }
                },
        )
    }
}
