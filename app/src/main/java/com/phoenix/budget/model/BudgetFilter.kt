package com.phoenix.budget.model

import android.arch.lifecycle.MutableLiveData
import com.phoenix.budget.model.viewmodel.ModelResponse
import io.reactivex.Flowable

/**
 * Created by Pushpan on 23/02/18.
 */
class BudgetFilter(val name: String, val type: BudgetFilterType, val filterFunction: () -> Flowable<MutableList<Record>>, val onMore:()->Unit) {
    val recordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()

    enum class BudgetFilterType {
        Reminder,
        Record
    }

    companion object {
        @JvmStatic
        val MAX_ROWS = "5"
        @JvmStatic
        val ALL_ROWS = Int.MAX_VALUE.toString()
    }
}