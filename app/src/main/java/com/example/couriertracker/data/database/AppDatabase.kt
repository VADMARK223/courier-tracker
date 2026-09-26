package com.example.couriertracker.data.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Operation

@Database(
    entities = [Category::class, Operation::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun operationDao(): OperationDao
}