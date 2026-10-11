package com.example.couriertracker.ui.category.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.couriertracker.data.model.Category

@Composable
fun CategoriesList(
    categories: List<Category>,
    onCategoryDelete: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (categories.isEmpty()) {
            Text(
                text = "Список категорий пуст",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = categories, key = { item -> item.id }) { item ->
                    CategoriesListItem(
                        item = item,
                        onDelete = { category -> onCategoryDelete(item) }
                    )
                }
            }
        }
    }
}