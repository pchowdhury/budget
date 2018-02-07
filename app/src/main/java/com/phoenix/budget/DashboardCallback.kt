package com.phoenix.budget

import com.phoenix.budget.model.Record

/**
 * Created by Pushpan on 26/01/18.
 */
interface DashboardCallback : ErrorCallback {
    fun updateRecords(list: List<Record>)
    fun getIconId(catogoryId: Int): Int
}