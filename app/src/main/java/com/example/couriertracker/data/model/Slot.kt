package com.example.couriertracker.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "slots")
data class Slot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val serviceId: Long,

    val startTime:Long,

    val endTime:Long,
)