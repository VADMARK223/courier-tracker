package com.example.couriertracker.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.couriertracker.AppConfig

@Composable
fun AppDrawer(
    settingsSelected: Boolean,
    exercisesSelected: Boolean,
    onSettingsClick: () -> Unit,
    onExercisesClick: () -> Unit,
) {
    ModalDrawerSheet {
        Text(
            text = "Courier Tracker (${AppConfig.VERSION})",
            modifier = Modifier.padding(16.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        NavigationDrawerItem(
            label = {
                Text("Настройки")
            },
            selected = settingsSelected,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = {
                Text("Упражнения")
            },
            selected = exercisesSelected,
            onClick = onExercisesClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null
                )
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}