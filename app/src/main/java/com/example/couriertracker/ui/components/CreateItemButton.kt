package com.example.couriertracker.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateItemButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true
) {
    val content: @Composable RowScope.() -> Unit = {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text)
    }

    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier,
            content = content
        )
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            content = content
        )
    }
}