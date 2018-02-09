package com.phoenix.budget

import com.phoenix.budget.model.Record

/**
 * Created by Pushpan on 26/01/18.
 */
interface ReportCallback : CategoryCallback, ErrorCallback {
    fun updateRecords(list: MutableList<Record>)
    fun reloadRecords()
    fun showReport(categoryId:Int)
}