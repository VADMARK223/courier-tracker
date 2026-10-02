package com.example.couriertracker.data.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Query("SELECT * FROM operations ORDER BY date DESC")
    fun getAll(): Flow<List<Operation>>

    @Transaction
    @Query("SELECT * FROM operations ORDER BY date DESC")
    fun getAllWithCategory(): Flow<List<OperationWithCategory>>

    @Insert
    suspend fun insert(operation: Operation)

    @Delete
    suspend fun delete(operation: Operation)
}