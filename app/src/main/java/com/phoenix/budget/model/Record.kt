package com.phoenix.budget.model

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by Pushpan on 01/02/18.
 */
@Entity(tableName = "records")
open class Record() {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "user_d")
    var userId: String = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "category_id")
    var categoryId: Int = -1

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

//    @Ignore
//    constructor(id: Long, userId: String, title: String, categoryId: Int, amount: Double, isIncome: Boolean, note: String, createdOn: Date, updatedOn: Date):this() {
//        this.id = id
//        this.title = title
//        this.userId = userId
//        this.categoryId = categoryId
//        this.amount = amount
//        this.isIncome = isIncome
//        this.note = note
//        this.createdOn = createdOn
//        this.updatedOn = updatedOn
//    }
//
//    @Ignore
//    constructor(userId: String, isIncome: Boolean):this(){
//        this.userId = userId
//        this.isIncome = isIncome
//    }
}