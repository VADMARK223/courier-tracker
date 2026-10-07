package com.example.couriertracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.couriertracker.data.model.operation.OperationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(
    private val context: Context
) {
    private val lastOperationTypeKey = stringPreferencesKey("last_operation_type")
    private val lastSelectedServiceKey = longPreferencesKey("last_selected_service")

    val lastOperationType: Flow<OperationType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences()) // Возвращаем пустые преференсы при ошибке чтения
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val savedValue = preferences[lastOperationTypeKey]

            if (savedValue != null) {
                OperationType.valueOf(savedValue)
            } else {
                OperationType.EXPENSE
            }
        }

    suspend fun saveLastOperationType(type: OperationType) {
        context.dataStore.edit { preferences ->
            preferences[lastOperationTypeKey] = type.name
        }
    }

    val lastSelectedServiceId: Flow<Long?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[lastSelectedServiceKey]
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