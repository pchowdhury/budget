package com.phoenix.budget

import com.phoenix.budget.model.Transaction

/**
 * Created by Pushpan on 28/01/18.
 */
interface TransactionCallback : ErrorCallback {
    fun onBindTransaction(transaction: Transaction)
}