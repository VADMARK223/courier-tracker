package com.example.couriertracker.data.repository

import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Operation
import com.example.couriertracker.data.model.OperationType
import com.example.couriertracker.data.model.OperationWithCategory
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    val data: Flow<List<OperationWithCategory>>
    suspend fun addCategory(category: Category)

    fun getCategoriesByType(type: OperationType): Flow<List<Category>>

    suspend fun addOperation(operation: Operation)

    suspend fun deleteOperation(operation: Operation)
}