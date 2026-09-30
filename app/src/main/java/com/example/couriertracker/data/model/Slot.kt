package com.example.couriertracker.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "slots")
data class Slot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val serviceId: Long,

    val startTime: LocalDateTime, // Room автоматически превращает это в INTEGER при сохранении в БД

    val endTime: LocalDateTime,
)