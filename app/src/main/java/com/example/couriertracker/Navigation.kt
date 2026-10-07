package com.example.couriertracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.couriertracker.ui.category.CategoriesScreen
import com.example.couriertracker.ui.exercise.ExercisesScreen
import com.example.couriertracker.ui.main.MainScreen
import com.example.couriertracker.ui.operation.OperationsScreen
import com.example.couriertracker.ui.service.ServicesScreen
import com.example.couriertracker.ui.settings.SettingsScreen
import com.example.couriertracker.ui.slot.SlotsScreen
import kotlinx.coroutines.launch

enum class TopLevelScreen(
    val navKey: NavKey
) {
    MAIN(Main),
    SERVICES(Services),
    SLOTS(Slots),
    OPERATIONS(Operations),
    CATEGORIES(Categories)
}

fun NavKey.toTopLevelScreen(): TopLevelScreen =
    when (this) {
        Main -> TopLevelScreen.MAIN
        Services -> TopLevelScreen.SERVICES
        Slots -> TopLevelScreen.SLOTS
        Operations -> TopLevelScreen.OPERATIONS
        Categories -> TopLevelScreen.CATEGORIES
        else -> TopLevelScreen.MAIN
    }


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
            name = "courier_tracker.db"
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

    val settingsRepository = remember { SettingsRepository(applicationContext) }
    val lastSelectedScreen by settingsRepository.lastSelectedScreen.collectAsState(
        initial = null
    )

    val backStack = rememberNavBackStack(Main)

    var screenRestored by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(lastSelectedScreen) {
        if (!screenRestored && lastSelectedScreen != null) {
            val screen = lastSelectedScreen!!.navKey

            backStack.clear()
            backStack.add(Main)

            if (screen != Main) {
                backStack.add(screen)
            }

            screenRestored = true
        }
    }

    fun saveSelectedScreen(screen: NavKey) {
        scope.launch {
            settingsRepository.saveLastSelectedScreen(
                screen.toTopLevelScreen()
            )
        }
    }

    fun navigateToTopLevel(screen: NavKey) {
        backStack.clear()
        backStack.add(Main)

        if (screen != Main) {
            backStack.add(screen)
        }

        saveSelectedScreen(screen)
    }

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
                onSettingsClick = { navigateFromDrawer(Settings) },
                onExercisesClick = { navigateFromDrawer(Exercises) }
            )
        }
    ) {
        AppScaffold(
            currentScreen = backStack.last(),

            onMainClick = { navigateToTopLevel(Main) },
            onServiceClick = { navigateToTopLevel(Services) },
            onSlotClick = { navigateToTopLevel(Slots) },
            onOperationClick = { navigateToTopLevel(Operations) },
            onCategoryClick = { navigateToTopLevel(Categories) }
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
                                    onBack = { backStack.removeLastOrNull() },
                                    repository = repository,
                                    settingsRepository = settingsRepository
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
                                )
                            }
                        }
                        entry<Operations> {
                            AppScreen {
                                OperationsScreen(
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
                        entry<Categories> {
                            AppScreen {
                                CategoriesScreen(
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
