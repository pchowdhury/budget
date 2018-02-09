package com.phoenix.budget

import com.phoenix.budget.model.CategorizedRecord

/**
 * Created by Pushpan on 28/01/18.
 */
interface ModifyRecordCallback : ErrorCallback {
    fun onBindRecord(categorizedRecord: CategorizedRecord)
    fun closeRecord()
    fun addAnotherRecord()
}