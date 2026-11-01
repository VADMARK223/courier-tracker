package com.example.couriertracker.data.repository

import androidx.room3.withWriteTransaction
import com.example.couriertracker.data.database.AppDatabase
import com.example.couriertracker.data.database.CategoryDao
import com.example.couriertracker.data.database.OperationDao
import com.example.couriertracker.data.database.ServiceDao
import com.example.couriertracker.data.database.SlotDao
import com.example.couriertracker.data.exception.ServiceAlreadyExistsException
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.operation.Operation
import com.example.couriertracker.data.model.operation.OperationType
import com.example.couriertracker.data.model.operation.OperationWithCategory
import com.example.couriertracker.data.model.slot.Slot
import com.example.couriertracker.data.model.slot.SlotWithService
import kotlinx.coroutines.flow.Flow

class DefaultDataRepository(
    private val db: AppDatabase,
    private val serviceDao: ServiceDao,
    private val slotDao: SlotDao,
    private val categoryDao: CategoryDao,
    private val operationDao: OperationDao
) : DataRepository {
    override val operationsWithCategory: Flow<List<OperationWithCategory>> =
        operationDao.getAllWithCategory()
    override val slotWithService: Flow<List<SlotWithService>> = slotDao.getAllWithService()

    override suspend fun insertService(service: Service) {
        if (serviceDao.getServiceByName(service.name) != null) {
            throw ServiceAlreadyExistsException()
        }

        serviceDao.insert(service)
    }

    override fun getServices(): Flow<List<Service>> {
        return serviceDao.getAll()
    }

    override suspend fun deleteService(service: Service) {
        serviceDao.delete(service)
    }

    override suspend fun insertSlot(slot: Slot) {
        slotDao.insert(slot)
    }

    override suspend fun deleteSlot(slot: Slot) {
        slotDao.delete(slot)
    }

    override fun getSlots(): Flow<List<Slot>> {
        return slotDao.getAll()
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAll()
    }

    override fun getCategoriesByType(type: OperationType): Flow<List<Category>> {
        return categoryDao.getByType(type)
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category)
    }

    override suspend fun addOperation(operation: Operation) {
        db.withWriteTransaction {
            val category = categoryDao.getById(
                operation.categoryId
            ) ?: error("Категория не найдена")

            val delta = when (category.type) {
                OperationType.INCOME -> operation.amount
                OperationType.EXPENSE -> -operation.amount
            }

            val updated = serviceDao.updateBalance(
                operation.serviceId,
                delta
            )

            check(updated == 1) {
                "Счёт не найден"
            }

//            error("Тест отката транзакции")

            operationDao.insert(operation)
        }
    }

    override suspend fun deleteOperation(operation: Operation) {
        operationDao.delete(operation)
    }
}