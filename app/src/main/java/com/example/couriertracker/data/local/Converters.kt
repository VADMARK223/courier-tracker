package com.example.couriertracker.data.local


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room3.ColumnTypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId = ZoneId.systemDefault()

    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnTypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), zoneId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnTypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(zoneId)?.toInstant()?.toEpochMilli()
    }
}