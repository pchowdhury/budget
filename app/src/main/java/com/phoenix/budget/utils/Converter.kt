package com.phoenix.budget.utils

import android.arch.persistence.room.TypeConverter
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.RecurringRecord
import com.phoenix.budget.model.RecurringRecord.RepeatType
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

    companion object {
        fun newRecordForRecurringRecord(recuringRecord: RecurringRecord): Record {
            return Record(recuringRecord.userId, recuringRecord.title, recuringRecord.categoryId, recuringRecord.amount, recuringRecord.isIncome, recuringRecord.note, false, recuringRecord.id, recuringRecord.createdFor, recuringRecord.updatedOn)
        }
    }

    @TypeConverter
    fun fromRepeatType(type: Int): RepeatType {
        return RepeatType.values()[type]
    }

    @TypeConverter
    fun toRepeatType(type: RepeatType): Int {
        return type.ordinal
    }
}