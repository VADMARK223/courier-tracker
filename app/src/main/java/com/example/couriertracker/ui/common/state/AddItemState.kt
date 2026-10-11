package com.example.couriertracker.ui.common.state

data class AddItemState (
    val isValid: Boolean,
    val errorText: String? = ""
)