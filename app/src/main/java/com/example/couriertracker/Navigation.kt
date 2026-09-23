package com.example.couriertracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.room3.Room
import com.example.couriertracker.data.AppDatabase
import com.example.couriertracker.data.DefaultDataRepository
import com.example.couriertracker.data.MIGRATION_1_2
import com.example.couriertracker.ui.AppScreen
import com.example.couriertracker.ui.category.AddCategoryScreen
import com.example.couriertracker.ui.main.MainScreen
import com.example.couriertracker.ui.operation.AddOperationScreen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val database = remember {
        Room.databaseBuilder<AppDatabase>(
            context = context.applicationContext,
            name = "courier_tacker.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    val repository = remember {
        DefaultDataRepository(database.categoryDao(), database.operationDao())
    }

    val backStack = rememberNavBackStack(Main)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
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
                        )
                    }
                }
            },
    )
}
