package com.example.couriertracker.data

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [Category::class, Operation::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun operationDao(): OperationDao
}