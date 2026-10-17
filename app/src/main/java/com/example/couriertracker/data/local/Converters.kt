package com.example.couriertracker.data.local


import androidx.room3.ColumnTypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    private val zoneId = ZoneId.systemDefault()

    @ColumnTypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), zoneId)
        }
    }

    @ColumnTypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(zoneId)?.toInstant()?.toEpochMilli()
    }
}