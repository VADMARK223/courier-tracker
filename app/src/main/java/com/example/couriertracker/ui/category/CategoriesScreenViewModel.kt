package com.example.couriertracker.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.couriertracker.AppConfig.TAG_LOGCAT
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoriesScreenViewModel(
    private val repository: DataRepository,
) : ViewModel() {

    fun getCategories(): Flow<List<Category>> {
        return repository.getCategories()
    }

    fun saveCategory(category: Category) {
        viewModelScope.launch {
            try {
                repository.insertCategory(category)
            } catch (e: Exception) {
                Log.e(TAG_LOGCAT, "Error saveCategory: = ${e.message}")
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

}