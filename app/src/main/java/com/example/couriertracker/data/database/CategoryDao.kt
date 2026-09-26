package com.example.couriertracker.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.OperationType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name")
    fun getByType(type: OperationType): Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category)
}