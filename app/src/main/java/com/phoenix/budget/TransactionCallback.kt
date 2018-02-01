package com.phoenix.budget

import com.phoenix.budget.model.Record

/**
 * Created by Pushpan on 28/01/18.
 */
interface TransactionCallback : ErrorCallback {
    fun onBindTransaction(Record: Record)
}