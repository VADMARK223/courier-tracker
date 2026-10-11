package com.example.couriertracker.ui.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.repository.DataRepository
import com.example.couriertracker.ui.category.list.CategoriesList
import com.example.couriertracker.ui.category.sheet.AddCategorySheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    repository: DataRepository,
    onBack: () -> Unit,
) {
    var showAddSheet by remember { mutableStateOf(false) }
    val viewModel: CategoriesScreenViewModel = viewModel {
        CategoriesScreenViewModel(repository = repository)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Категории")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddSheet = true
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить категорию"
                )
            }
        },
        content = { paddingValues ->
            val categories by viewModel.getCategories().collectAsStateWithLifecycle(emptyList())

            CategoriesList(
                categories,
                onCategoryDelete = { category -> viewModel.deleteCategory(category) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )

    if (showAddSheet) {
        ModalBottomSheet(onDismissRequest = { showAddSheet = false }) {
            AddCategorySheet(viewModel = viewModel, onSaved = { showAddSheet = false })
        }
    }
}
