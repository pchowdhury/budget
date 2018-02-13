package com.phoenix.budget.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Pushpan on 01/02/18.
 */
@Entity(tableName = "recurring_records")
open class RecurringRecord() : Record() {
    @ColumnInfo(name = "required_approval")
    var requiredApproval: Boolean = false

    @ColumnInfo(name = "frequency")
    var frequency: Int = RepeatType.RepeatOnce

    @ColumnInfo(name = "next_update_on")
    var nextUpdateOn: Date = Date()

    @Ignore
    constructor(userId: String, title: String, categoryId: Int, amount: Double, requiredApproval: Boolean, isIncome: Boolean, note: String, frequency: Int, createdFor: Date, updatedOn: Date) : this() {
        this.id = id
        this.title = title
        this.userId = userId
        this.categoryId = categoryId
        this.amount = amount
        this.requiredApproval = requiredApproval
        this.isIncome = isIncome
        this.note = note
        this.frequency = frequency
        this.createdFor = createdFor
        this.updatedOn = updatedOn
    }

    companion object RepeatType {
        @JvmStatic
        val RepeatOnce: Int = 0
        val RepeatWeekly: Int = 1
        val RepeatMonthly: Int = 2
        val RepeatQuarterly: Int = 3
        val RepeatYearly: Int = 4
    }

}