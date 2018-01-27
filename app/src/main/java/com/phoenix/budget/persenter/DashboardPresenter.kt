package com.phoenix.budget.persenter

import com.phoenix.budget.DashboardCallback

/**
 * Created by Pushpan on 26/01/18.
 */
class DashboardPresenter(thisDashboard: DashboardCallback) {
    var dashboard: DashboardCallback = thisDashboard
    fun selectExpense() {
        dashboard.startTransaction()
    }

    fun selectFixedIncome() {
        dashboard.startTransaction()
    }

    fun selectFixedExpense() {
        dashboard.startTransaction()
    }

    fun selectIncome() {
        dashboard.startTransaction()
    }


}