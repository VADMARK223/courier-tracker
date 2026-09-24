package com.example.couriertracker.data

import kotlinx.coroutines.flow.Flow

interface DataRepository {
    val data: Flow<List<OperationWithCategory>>
    suspend fun addCategory(category: Category)

    fun getCategoriesByType(type: OperationType): Flow<List<Category>>

    suspend fun addOperation(operation: Operation)

    suspend fun deleteOperation(operation: Operation)
}

class DefaultDataRepository(
    private val categoryDao: CategoryDao,
    private val operationDao: OperationDao
) : DataRepository {
    override val data: Flow<List<OperationWithCategory>> = operationDao.getAllWithCategory()

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
