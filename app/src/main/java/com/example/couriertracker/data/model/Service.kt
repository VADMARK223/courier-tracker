package com.example.couriertracker.data.model

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(tableName = "services", indices = [Index(value = ["name"], unique = true)])
data class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: Long = 0L // В копейках
)