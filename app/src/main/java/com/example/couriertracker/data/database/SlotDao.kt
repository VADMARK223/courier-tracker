package com.example.couriertracker.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot
import kotlinx.coroutines.flow.Flow

@Dao
interface SlotDao {
    @Query("SELECT * FROM slots ORDER BY startTime")
    fun getAll(): Flow<List<Slot>>

    @Insert
    suspend fun insert(slot: Slot)
}