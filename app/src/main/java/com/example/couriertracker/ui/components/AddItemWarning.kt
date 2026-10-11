package com.example.couriertracker.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.couriertracker.ui.common.state.AddItemState

@Composable
fun AddItemWarning(state: AddItemState) {
    state.errorText?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}