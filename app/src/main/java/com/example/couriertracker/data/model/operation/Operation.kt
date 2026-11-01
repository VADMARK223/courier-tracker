package com.example.couriertracker.data.model.operation

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val serviceId: Long,
    val categoryId: Long,

    val amount: Long,
    val date: Long,

    val slotId: Long? = null
)