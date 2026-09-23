package com.example.couriertracker.data

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal val MIGRATION_1_2 = object : Migration(1, 2) {

    override suspend fun migrate(connection: SQLiteConnection) {

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                type TEXT NOT NULL
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            ALTER TABLE operations
            ADD COLUMN categoryId INTEGER DEFAULT NULL
            """.trimIndent()
        )
    }
}