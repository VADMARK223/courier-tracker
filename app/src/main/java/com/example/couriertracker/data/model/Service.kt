package com.example.couriertracker.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "services")
data class Service (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String
)