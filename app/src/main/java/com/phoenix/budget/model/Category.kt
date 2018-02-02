package com.phoenix.budget.model

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by Pushpan on 28/01/18.
 */

@Entity(tableName = "category")
class Category() {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "is_custom")
    var isCustom: Boolean = false

    @ColumnInfo(name = "created_on")
    var createdOn: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "updated_on")
    var updatedOn: Date = Date(System.currentTimeMillis())

    @Ignore
    constructor(id: Int, title: String, isCustom: Boolean,  createdOn: Date, updatedOn: Date) : this() {
        this.title = title
        this.id = id
        this.isCustom = isCustom
        this.createdOn = createdOn
        this.updatedOn = updatedOn
    }

    @Ignore
    constructor(title: String, isCustom: Boolean, createdOn: Date, updatedOn: Date) : this(0, title, isCustom, createdOn, updatedOn)
}