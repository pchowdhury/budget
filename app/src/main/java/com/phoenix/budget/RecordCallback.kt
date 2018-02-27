package com.phoenix.budget

import com.phoenix.budget.model.Record

/**
 * Created by Pushpan on 26/01/18.
 */
interface RecordCallback : CategoryCallback {
//    fun updateRecentRecords(list: MutableList<Record>)
//    fun updateReminders(list: MutableList<Record>)
//    fun loadRecords()
//    fun loadReminders()
    fun showReport(filter: String)

    fun markReminderDone(record: Record)

    fun onRemoveRecord(record: Record)
}