package com.example.couriertracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.couriertracker.TopLevelScreen
import com.example.couriertracker.data.model.operation.OperationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(
    private val context: Context
) {
    private val lastSelectedScreenKey = stringPreferencesKey("last_selected_screen")

    private val lastOperationTypeKey = stringPreferencesKey("last_operation_type")
    private val lastSelectedServiceKey = longPreferencesKey("last_selected_service")

    private val preferences: Flow<Preferences> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }

    val lastSelectedScreen: Flow<TopLevelScreen> =
        preferences.map { preferences ->
            preferences[lastSelectedScreenKey]
                ?.let { savedValue ->
                    runCatching {
                        TopLevelScreen.valueOf(savedValue)
                    }.getOrNull()
                }
                ?: TopLevelScreen.MAIN
        }

    val lastOperationType: Flow<OperationType> =
        preferences.map { preferences ->
            preferences[lastOperationTypeKey]
                ?.let { savedValue ->
                    runCatching {
                        OperationType.valueOf(savedValue)
                    }.getOrNull()
                }
                ?: OperationType.EXPENSE
        }

    val lastSelectedServiceId: Flow<Long?> =
        preferences.map { preferences ->
            preferences[lastSelectedServiceKey]
        }

    suspend fun saveLastOperationType(type: OperationType) {
        context.dataStore.edit { preferences ->
            preferences[lastOperationTypeKey] = type.name
        }
    }

    suspend fun saveLastSelectedScreen(screen: TopLevelScreen) {
        context.dataStore.edit { preferences ->
            preferences[lastSelectedScreenKey] = screen.name
        }
    }

    suspend fun saveLastSelectedServiceId(serviceId: Long) {
        context.dataStore.edit { preferences ->
            preferences[lastSelectedServiceKey] = serviceId
        }
    }

    suspend fun clearLastSelectedServiceId() {
        context.dataStore.edit { preferences ->
            preferences.remove(lastSelectedServiceKey)
        }
    }

    suspend fun clearLastSelectedServiceIdIfMatches(serviceId: Long) {
        context.dataStore.edit { preferences ->
            if (preferences[lastSelectedServiceKey] == serviceId) {
                preferences.remove(lastSelectedServiceKey)
            }
        }
    }
}