package com.phoenix.budget.presenter

import com.phoenix.budget.ReportCallback
import com.phoenix.budget.model.Record
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
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

    fun loadRecordsByCategoryId(categoryId: Int, limit: Int) {
        if (limit == -1) {
            compositeDisposable.add(BudgetApp.database.RecordsDao().findRecordsByCategoryId(categoryId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> reportCallback.updateRecords(list) }, { error -> showError(error) }))
        }else{
            compositeDisposable.add(BudgetApp.database.RecordsDao().findRecordsByCategoryId(categoryId, limit)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> reportCallback.updateRecords(list) }, { error -> showError(error) }))
        }
    }

    fun removeDashboardRecord(record: Record) {
        Single.create(SingleOnSubscribe<Int> { emitter ->
            try {
                val ids = BudgetApp.database.RecordsDao().deleteRecord(record)
                emitter.onSuccess(ids)
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Int>() {
                    override fun onSuccess(ids: Int) {
                        reportCallback.reloadRecords()
                    }

                    override fun onError(e: Throwable) {
                        showError(e)
                    }
                })
    }

    fun showError(t: Throwable) {

    }

    fun getIconId(catogoryId: Int): Int = reportCallback.getIconId(catogoryId)

}