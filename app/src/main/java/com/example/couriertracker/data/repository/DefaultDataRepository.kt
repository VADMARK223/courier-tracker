package com.example.couriertracker.data.repository

import com.example.couriertracker.data.database.CategoryDao
import com.example.couriertracker.data.database.OperationDao
import com.example.couriertracker.data.database.ServiceDao
import com.example.couriertracker.data.database.SlotDao
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Operation
import com.example.couriertracker.data.model.OperationType
import com.example.couriertracker.data.model.OperationWithCategory
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot
import kotlinx.coroutines.flow.Flow

class DefaultDataRepository(
    private val serviceDao: ServiceDao,
    private val slotDao: SlotDao,
    private val categoryDao: CategoryDao,
    private val operationDao: OperationDao
) : DataRepository {
    override val data: Flow<List<OperationWithCategory>> = operationDao.getAllWithCategory()

    override suspend fun addService(service: Service) {
        serviceDao.insert(service)
    }

    override suspend fun addSlot(slot: Slot) {
        slotDao.insert(slot)
    }

    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category)
    }

    override fun getCategoriesByType(
        type: OperationType
    ): Flow<List<Category>> {
        return categoryDao.getByType(type)
    }

    override suspend fun addOperation(operation: Operation) {
        operationDao.insert(operation)
    }

    override suspend fun deleteOperation(operation: Operation) {
        operationDao.delete(operation)
    }
}