package com.example.couriertracker.data.model.slot

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.example.couriertracker.data.model.Service
import java.time.LocalDateTime

@Entity(
    tableName = "slots",
    foreignKeys = [
        ForeignKey(
            entity = Service::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Slot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val serviceId: Long,

    val startTime: LocalDateTime, // Room автоматически превращает это в INTEGER при сохранении в БД

    val endTime: LocalDateTime,
)