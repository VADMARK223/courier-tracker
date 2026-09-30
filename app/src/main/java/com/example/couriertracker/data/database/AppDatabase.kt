package com.example.couriertracker.data.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.couriertracker.data.local.Converters
import com.example.couriertracker.data.model.Category
import com.example.couriertracker.data.model.Operation
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.model.Slot

@Database(
    entities = [
        Service::class,
        Slot::class,
        Category::class,
        Operation::class
    ],
    version = 3
)
//@TypeConverters
@ColumnTypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun slotDao(): SlotDao
    abstract fun categoryDao(): CategoryDao
    abstract fun operationDao(): OperationDao
}