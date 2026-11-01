package com.example.couriertracker.ui.category.sheet

import androidx.lifecycle.ViewModel
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.repository.DataRepository

class AddCategoryViewModel(private val repository: DataRepository) : ViewModel() {
    suspend fun saveCategory(category: Category) {
        repository.insertCategory(category)
    }
}