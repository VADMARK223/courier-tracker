package com.example.couriertracker.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Transaction
    @Query("SELECT * FROM operations ORDER BY date DESC")
    fun getAllWithCategory():Flow<List<OperationWithCategory>>

    @Query("SELECT * FROM operations ORDER BY date DESC")
    fun getAll(): Flow<List<Operation>>

    @Insert
    suspend fun insert(operation: Operation)

    @Delete
    suspend fun delete(operation: Operation)
}