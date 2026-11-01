package com.example.couriertracker.ui.operation

data class OperationsUiState(
    val hasServices: Boolean = false,
    val hasCategories: Boolean = false
) {
    val canAddOperation: Boolean
        get() = hasServices && hasCategories
}
