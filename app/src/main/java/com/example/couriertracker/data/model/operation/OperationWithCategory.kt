package com.example.couriertracker.data.model.operation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.couriertracker.data.model.Category

data class OperationWithCategory (
    @Embedded
    val operation: Operation,
    @Relation(
        parentColumns = ["categoryId"],
        entityColumns = ["id"]
    )
    val category: Category?
)