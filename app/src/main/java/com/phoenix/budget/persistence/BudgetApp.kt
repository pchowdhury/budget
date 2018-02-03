package com.phoenix.budget.persistence

import android.app.Application
import android.arch.persistence.room.Room
import com.phoenix.budget.R
import com.phoenix.budget.model.Category
import io.reactivex.schedulers.Schedulers
import java.sql.Date


/**
 * Created by Pushpan on 28/01/18.
 */
class BudgetApp : Application() {

    companion object {
        lateinit var database: BudgetDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room
                .databaseBuilder(this, BudgetDatabase::class.java, "budget_db")
//                .addCallback(callback)
                .build()
        checkForFirstRun()
    }

    fun checkForFirstRun() {
        BudgetApp.database.categoryDao().getAllCategory()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe { list -> if (list.isEmpty()) addCategories() }
    }

    private fun addCategories() {
        val categories = applicationContext.resources.getStringArray(R.array.category_items)
        for (i in 1..(categories.size)) {
            val item = Category(i, categories[i-1], false, Date(System.currentTimeMillis()), Date(System.currentTimeMillis()))
            database.categoryDao().insertTask(item)
        }
    }
}