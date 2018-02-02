package com.phoenix.budget.model

import android.arch.persistence.room.Ignore
import java.util.*

/**
 * Created by Pushpan on 02/02/18.
 */
class CategorizedRecord: Record{

    var categoryTitle: String = ""

    var isCustom: Boolean = false

    var categoryCreatedOn: Date = Date(System.currentTimeMillis())

    var categoryUpdatedOn: Date = Date(System.currentTimeMillis())

    constructor()
    @Ignore
    constructor(userId: String, isIncome: Boolean){
        this.userId = userId
        this.isIncome = isIncome
    }

    fun getCategory(): Category {
        return Category(categoryId, categoryTitle, isCustom, categoryCreatedOn, categoryUpdatedOn)
    }

    fun getRecord(): Record{
        return Record(userId, title, categoryId, amount, isIncome, note, createdOn, updatedOn)
    }
}