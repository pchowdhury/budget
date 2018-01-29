package com.phoenix.budget.persistence

import android.app.Application
import android.arch.persistence.room.Room

/**
 * Created by Pushpan on 28/01/18.
 */
class BudgetApp : Application() {

    companion object {
        lateinit var database: BudgetDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, BudgetDatabase::class.java, "budget_db").build()
    }
}