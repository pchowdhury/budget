package com.phoenix.budget.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Pushpan on 28/01/18.
 */

@Entity(tableName = "category")
class Category() {
    @ColumnInfo(name = "category_id")
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "created_on")
    var createdOn: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "updated_on")
    var updatedOn: Date = Date(System.currentTimeMillis())

    @Ignore
    constructor(categoryId: Int, title: String, createdOn: Date, updatedOn: Date) : this() {
        this.title = title
        this.categoryId = categoryId
        this.createdOn = createdOn
        this.updatedOn = updatedOn
    }

    @Ignore
    constructor(title: String, createdOn: Date, updatedOn: Date) : this(0, title,  createdOn, updatedOn) {
        this.title = title
        this.categoryId = categoryId
        this.createdOn = createdOn
        this.updatedOn = updatedOn
    }
}