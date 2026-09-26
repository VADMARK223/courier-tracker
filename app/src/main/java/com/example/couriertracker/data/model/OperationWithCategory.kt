package com.example.couriertracker.data.model

import androidx.room3.Embedded
import androidx.room3.Relation

data class OperationWithCategory (
    @Embedded
    val operation: Operation,
    @Relation(
        parentColumns = ["categoryId"],
        entityColumns = ["id"]
    )
    val category: Category?
)