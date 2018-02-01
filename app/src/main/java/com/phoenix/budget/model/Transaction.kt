package com.phoenix.budget.model

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by Pushpan on 01/02/18.
 */
@Entity(tableName = "transaction")
class Transaction() {
    @ColumnInfo(name = "iid")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "user_d")
    var userId: String = "00"

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "category_id")
    var categoryId: Int = 0

//    @Ignore
//    var category = Category()

    @ColumnInfo(name = "amount")
    var amount: Double = Double.MIN_VALUE

    @ColumnInfo(name = "is_income")
    var isIncome: Boolean = false

    @ColumnInfo(name = "note")
    var note: String = ""

    @ColumnInfo(name = "created_on")
    var createdOn: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "updated_on")
    var updatedOn: Date = Date(System.currentTimeMillis())

    @Ignore
    constructor(id: Int, userId: String, title: String, categoryId: Int, amount: Double, isIncome: Boolean, note: String, createdOn: Date, updatedOn: Date):this() {
        this.id = id
        this.title = title
        this.userId = userId
        this.categoryId = categoryId
        this.amount = amount
        this.isIncome = isIncome
        this.note = note
        this.createdOn = createdOn
        this.updatedOn = updatedOn
//        this.category.categoryId = categoryId
    }

    @Ignore
    constructor(userId: String, isIncome: Boolean): this(){
        this.userId = userId
        this.isIncome = isIncome
    }
}