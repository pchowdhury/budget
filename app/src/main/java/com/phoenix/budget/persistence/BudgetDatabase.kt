package com.phoenix.budget.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.phoenix.budget.model.Category
import com.phoenix.budget.model.Transaction
import com.phoenix.budget.model.dao.CategoryDao
import com.phoenix.budget.model.dao.TransactionDao
import com.phoenix.budget.utils.Converter

/**
 * Created by Pushpan on 28/01/18.
 */
@Database(entities = arrayOf(Category::class, Transaction::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}