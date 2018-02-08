package com.phoenix.budget.presenter

import com.phoenix.budget.DashboardCallback
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Pushpan on 26/01/18.
 */
class DashboardPresenter(thisDashboard: DashboardCallback) {
    var dashboardCallback: DashboardCallback = thisDashboard

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        loadReports()
    }

    fun loadReports() {
        compositeDisposable.add(BudgetApp.database.RecordsDao().findAllRecords()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> dashboardCallback.updateRecords(list) }, { error -> showError(error) }))
    }

    fun showError(t: Throwable) {

    }

    fun getIconId(catogoryId: Int): Int = dashboardCallback.getIconId(catogoryId)

}