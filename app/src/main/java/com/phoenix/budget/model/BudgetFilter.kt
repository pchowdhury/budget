package com.phoenix.budget.model

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.phoenix.budget.model.viewmodel.ModelResponse
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Flowable

/**
 * Created by Pushpan on 23/02/18.
 */
class BudgetFilter(val name: String, val type: BudgetFilterType, val filterFunction: () -> Flowable<MutableList<Record>>) {
    val recordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()

    enum class BudgetFilterType {
        Reminder,
        Record
    }

    companion object {
        @JvmStatic
        val RECENT_EVENTS = "Recent events"
        @JvmStatic
        val RECENT_EXPENSES = "Recent Expenses"
        @JvmStatic
        val RECENT_INCOMES = "Recent Incomes"
        @JvmStatic
        val UPCOMING_EVENTS = "Upcoming events"
        @JvmStatic
        val CATEGORY_ID = "categoryId:"

        @JvmStatic
        val MAX_ROWS = "5"
        @JvmStatic
        val ALL_ROWS = Int.MAX_VALUE.toString()

        @JvmStatic
        fun getFilterFor(filter: String, isLimted: Boolean): BudgetFilter {
            when (filter) {
                RECENT_EVENTS -> return BudgetFilter(RECENT_EVENTS, BudgetFilter.BudgetFilterType.Record, {
                    BudgetApp.database.recordsDao().findRecentRecords(if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                })
                RECENT_EXPENSES ->
                    return BudgetFilter(RECENT_EXPENSES, BudgetFilter.BudgetFilterType.Record, {
                        BudgetApp.database.recordsDao().findRecentExpenses(if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                    })
                RECENT_INCOMES ->
                    return BudgetFilter(RECENT_INCOMES, BudgetFilter.BudgetFilterType.Record, {
                        BudgetApp.database.recordsDao().findRecentIncomes(if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                    })
                UPCOMING_EVENTS ->
                    return BudgetFilter(UPCOMING_EVENTS, BudgetFilter.BudgetFilterType.Reminder, {
                        BudgetApp.database.recordsDao().findReminderRecords(if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                    })
                else ->
                    if (filter.contains(CATEGORY_ID) && !TextUtils.isEmpty(filter.substring(CATEGORY_ID.length))) {
                        return getFilterForCategoryId(filter, isLimted)
                    } else {
                       return  BudgetFilter(RECENT_EVENTS, BudgetFilter.BudgetFilterType.Record, {
                            BudgetApp.database.recordsDao().findRecentRecords(if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                        })
                    }
            }
        }

        @JvmStatic
        fun getFilterForCategoryId(filter: String, isLimted: Boolean): BudgetFilter {
               val categoryId = filter.substring(CATEGORY_ID.length).toInt()
                return BudgetFilter(RECENT_INCOMES, BudgetFilter.BudgetFilterType.Record, {
                    BudgetApp.database.recordsDao().findRecordsByCategoryId(categoryId, if (isLimted) BudgetFilter.MAX_ROWS else BudgetFilter.ALL_ROWS)
                })
        }
    }
}