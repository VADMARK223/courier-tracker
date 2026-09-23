package com.example.couriertracker.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val type: OperationType,
    val amount: Long,
    val date: Long,

    val categoryId: Long? = null
)
