package com.example.couriertracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Main : NavKey

@Serializable
data object Services : NavKey

@Serializable
data object Slots : NavKey



@Serializable
data object Operations : NavKey

@Serializable
data object Categories : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object Exercises : NavKey