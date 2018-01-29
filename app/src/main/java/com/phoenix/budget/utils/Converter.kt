package com.phoenix.budget.utils

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by Pushpan on 29/01/18.
 */
class Converter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return when (value) {
            null -> null
            else -> Date(value)
        }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {

        return when (date) {
            null -> null
            else -> date.time
        }
    }
}