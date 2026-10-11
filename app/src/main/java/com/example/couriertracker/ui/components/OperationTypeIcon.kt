package com.example.couriertracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun OperationTypeIcon(
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (isIncome) {
            Icons.Filled.ArrowCircleUp
        } else {
            Icons.Filled.ArrowCircleDown
        },
        contentDescription = if (isIncome) {
            "Доход"
        } else {
            "Расход"
        },
        tint = if (isIncome) {
            Color(0xFF2EAD62)
        } else {
            Color(0xFFE04444)
        },
        modifier = modifier
    )
}