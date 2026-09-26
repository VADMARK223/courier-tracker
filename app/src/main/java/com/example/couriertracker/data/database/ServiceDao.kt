package com.example.couriertracker.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.couriertracker.data.model.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY name")
    fun getAll(): Flow<List<Service>>

    @Insert
    suspend fun insert(service: Service)
}