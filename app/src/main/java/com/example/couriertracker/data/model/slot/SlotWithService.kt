package com.example.couriertracker.data.model.slot

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.couriertracker.data.model.Service

data class SlotWithService (
    @Embedded
    val slot: Slot,

    @Relation(
        parentColumns = ["serviceId"],
        entityColumns = ["id"]
    )
    val service: Service
)
