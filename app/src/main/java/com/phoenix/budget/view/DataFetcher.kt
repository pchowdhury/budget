package com.phoenix.budget.view

import com.phoenix.budget.model.Record

/**
 * Created by Pushpan on 28/02/18.
 */
interface DataFetcher {
    fun getDataAtPosition(position: Int): Record
}