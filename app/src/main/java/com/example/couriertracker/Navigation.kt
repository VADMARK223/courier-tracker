package com.example.couriertracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.couriertracker.data.database.AppDatabase
import com.example.couriertracker.data.database.MIGRATION_1_2
import com.example.couriertracker.data.database.MIGRATION_2_3
import com.example.couriertracker.data.repository.DefaultDataRepository
import com.example.couriertracker.data.repository.SettingsRepository
import com.example.couriertracker.ui.AppDrawer
import com.example.couriertracker.ui.AppScaffold
import com.example.couriertracker.ui.AppScreen
import com.example.couriertracker.ui.category.AddCategoryScreen
import com.example.couriertracker.ui.exercise.ExercisesScreen
import com.example.couriertracker.ui.main.MainScreen
import com.example.couriertracker.ui.operation.AddOperationScreen
import com.example.couriertracker.ui.service.ServicesScreen
import com.example.couriertracker.ui.settings.SettingsScreen
import com.example.couriertracker.ui.slot.SlotsScreen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val database = remember {
        Room.databaseBuilder<AppDatabase>(
            context = applicationContext,
            name = "courier_tacker.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override suspend fun onOpen(connection: SQLiteConnection) {
                    super.onOpen(connection)
                    connection.execSQL("PRAGMA foreign_keys=ON;")
                }
            })
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    val repository = remember {
        DefaultDataRepository(
            database.serviceDao(),
            database.slotDao(),
            database.categoryDao(),
            database.operationDao()
        )
    }

    val settingsRepository = remember {
        SettingsRepository(applicationContext)
    }

    val backStack = rememberNavBackStack(Main) // TODO: сделать сохранение выбранного экрана

    fun navigateFromDrawer(screen: NavKey) {
        backStack.clear()
        backStack.add(Main)
        backStack.add(screen)

        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                settingsSelected = backStack.last() == Settings,
                exercisesSelected = backStack.last() == Exercises,
                onSettingsClick = {
                    navigateFromDrawer(Settings)
                },
                onExercisesClick = {
                    navigateFromDrawer(Exercises)
                }
            )
        }
    ) {
        AppScaffold(
            currentScreen = backStack.last(),

            onMainClick = {
                backStack.clear()
                backStack.add(Main)
            },
            onServiceClick = {
                backStack.add(Services)
            },
            onSlotClick = {
                backStack.add(Slots)
            },
            onOperationClick = {
                backStack.add(AddOperation)
            },
            onCategoryClick = {
                backStack.add(AddCategory)
            }
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
                                    repository = repository,
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                            }
                        }

                        entry<Services> {
                            AppScreen {
                                ServicesScreen(
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    },
                                    repository = repository
                                )
                            }
                        }

                        entry<Slots> {
                            AppScreen {
                                SlotsScreen(
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    },
                                    repository = repository,
                                    settingsRepository = settingsRepository,

                                    onSlotDelete = { slot ->
                                        scope.launch {
                                            repository.deleteSlot(slot)
                                        }
                                    },
                                    onSave = { slot ->
                                        scope.launch {
                                            repository.addSlot(slot)
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
                        entry<Settings> {
                            AppScreen {
                                SettingsScreen(
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                        }
                        entry<Exercises> {
                            AppScreen {
                                ExercisesScreen(
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                        }
                    }
            )
        }
    }
}
