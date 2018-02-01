package com.phoenix.budget.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Pushpan on 01/02/18.
 */
@Entity(tableName = "trasaction")
class Trasaction {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "user_d")
    var userId: String = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "category_id")
    var categoryId: Int = 0

    @ColumnInfo(name = "amount")
    var amount: Double = Double.MIN_VALUE

    @ColumnInfo(name = "is_income")
    var isIncome: Boolean = false

    @ColumnInfo(name = "created_on")
    var createdOn: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "updated_on")
    var updatedOn: Date = Date(System.currentTimeMillis())

    constructor(id: Int, userId: String, title: String, categoryId: Int, amount: Double, isIncome: Boolean, createdOn: Date, updatedOn: Date) {
        this.id = id
        this.title = title
        this.userId = userId
        this.categoryId = categoryId
        this.amount = amount
        this.isIncome = isIncome
        this.createdOn = createdOn
        this.updatedOn = updatedOn
    }

    @Ignore
    constructor(userId: String, title: String, categoryId: Int, amount: Double, isIncome: Boolean, createdOn: Date, updatedOn: Date)
}