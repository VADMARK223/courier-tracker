package com.example.couriertracker.data.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import kotlinx.coroutines.flow.Flow

@Dao
interface SlotDao {
    @Query("SELECT * FROM slots ORDER BY startTime")
    fun getAll(): Flow<List<Slot>>

    @Insert
    suspend fun insert(slot: Slot)

    @Delete
    suspend fun delete(slot: Slot)

    @Transaction
    @Query("SELECT * FROM slots ORDER BY startTime")
    fun getAllWithService(): Flow<List<SlotWithService>>
}