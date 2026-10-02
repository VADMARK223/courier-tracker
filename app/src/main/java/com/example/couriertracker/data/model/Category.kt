package com.example.couriertracker.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.couriertracker.data.model.operation.OperationType

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val type: OperationType
)