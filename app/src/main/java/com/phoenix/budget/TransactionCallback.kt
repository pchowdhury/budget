package com.phoenix.budget

import com.phoenix.budget.model.CategorizedRecord

/**
 * Created by Pushpan on 28/01/18.
 */
interface TransactionCallback : ErrorCallback {
    fun onBindRecord(categorizedRecord: CategorizedRecord)
}