package com.phoenix.budget.model

/**
 * Created by Pushpan on 23/02/18.
 */
enum class SearchFilter(future: Boolean) {
    Recent(false),
    RecentExpense(false),
    RecentIncome(false),
    Reminders(true);

    val isFuture = future

}