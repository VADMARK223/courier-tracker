package com.example.couriertracker.ui.operation

import androidx.lifecycle.ViewModel
import com.example.couriertracker.data.Category
import com.example.couriertracker.data.DataRepository
import com.example.couriertracker.data.OperationType
import kotlinx.coroutines.flow.Flow

class AddOperationViewModel(
    private val repository: DataRepository
) : ViewModel() {
    fun getRepositories(type: OperationType): Flow<List<Category>> {
        return repository.getCategoriesByType(type)
    }
}