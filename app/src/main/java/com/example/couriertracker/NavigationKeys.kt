package com.example.couriertracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Main : NavKey

@Serializable
data object AddCategory : NavKey

@Serializable
data object AddOperation : NavKey