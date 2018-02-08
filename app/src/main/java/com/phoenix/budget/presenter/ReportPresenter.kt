package com.phoenix.budget.presenter

import com.phoenix.budget.ReportCallback
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Pushpan on 26/01/18.
 */
class ReportPresenter(thisReport: ReportCallback) {
    var reportCallback: ReportCallback = thisReport

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

//    init {
//        loadReports()
//    }

    fun loadRecords(limit: Int) {
        if (limit == -1) {
            compositeDisposable.add(BudgetApp.database.RecordsDao().findAllRecords()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> reportCallback.updateRecords(list) }, { error -> showError(error) }))

        } else {
            compositeDisposable.add(BudgetApp.database.RecordsDao().findAllRecords(limit)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> reportCallback.updateRecords(list) }, { error -> showError(error) }))

        }
    }

    fun loadRecordsById(categoryId: Int, limit: Int) {
            compositeDisposable.add(BudgetApp.database.RecordsDao().findRecordsByCategoryId(categoryId, limit)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> reportCallback.updateRecords(list) }, { error -> showError(error) }))
    }

    fun showError(t: Throwable) {

    }

    fun getIconId(catogoryId: Int): Int = reportCallback.getIconId(catogoryId)

}