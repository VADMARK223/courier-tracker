package com.example.couriertracker.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.couriertracker.util.getAppVersion

@Composable
fun AppVersionText() {
    val context = LocalContext.current

    val version = remember(context) {
        context.getAppVersion()
    }

    Text(
        text = "Версия $version",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}