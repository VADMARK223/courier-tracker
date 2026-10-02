package com.example.couriertracker.data.repository

import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    val operationsWithCategory: Flow<List<OperationWithCategory>>
    val slotWithService: Flow<List<SlotWithService>>

    suspend fun addService(service: Service)
    fun getServices(): Flow<List<Service>>
    suspend fun deleteService(service: Service)

    suspend fun addSlot(slot: Slot)
    fun getSlots(): Flow<List<Slot>>
    suspend fun deleteSlot(slot: Slot)
    suspend fun addCategory(category: Category)

    fun getCategoriesByType(type: OperationType): Flow<List<Category>>

    suspend fun addOperation(operation: Operation)

    suspend fun deleteOperation(operation: Operation)
}