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

    @TypeConverter
    fun fromToggle(value: Int?): Boolean? {
        return when (value) {
            1 -> true
            0->false
            else -> false
        }
    }

    @TypeConverter
    fun toToggle(value: Boolean?): Int? {
        return when (value) {
            true -> 1
            else -> 0
        }
    }
}