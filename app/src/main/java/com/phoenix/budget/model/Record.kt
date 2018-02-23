package com.phoenix.budget.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.text.TextUtils
import java.util.*

/**
 * Created by Pushpan on 01/02/18.
 */
@Entity(tableName = "records")
open class Record() {
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    @ColumnInfo(name = "user_id")
    var userId: String = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "category_id")
    var categoryId: Int = -1

    @ColumnInfo(name = "amount")
    var amount: Double = 0.toDouble()

    @ColumnInfo(name = "is_income")
    var isIncome: Boolean = false

    @ColumnInfo(name = "note")
    var note: String = ""

    @ColumnInfo(name = "done")
    var done: Boolean = false

    @ColumnInfo(name = "associated_id")
    var associatedId: String = ""

    @ColumnInfo(name = "created_for")
    var createdFor: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "updated_on")
    var updatedOn: Date = Date(System.currentTimeMillis())

    @Ignore
    constructor(userId: String, title: String, categoryId: Int, amount: Double, isIncome: Boolean, note: String, done: Boolean, associatedId: String, createdFor: Date, updatedOn: Date):this() {
        this.title = title
        this.userId = userId
        this.categoryId = categoryId
        this.amount = amount
        this.isIncome = isIncome
        this.note = note
        this.done = done
        this.associatedId = associatedId
        this.createdFor = createdFor
        this.updatedOn = updatedOn
    }

    fun isReminder(): Boolean = !done

    fun hasNote():Boolean {
        return TextUtils.isEmpty(note)
    }

}