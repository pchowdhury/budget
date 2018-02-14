package com.phoenix.budget.presenter

import com.phoenix.budget.RecordCallback
import com.phoenix.budget.model.Record
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Pushpan on 26/01/18.
 */
class RecordPresenter(thisRecord: RecordCallback) {
    var recordCallback: RecordCallback = thisRecord

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

//    fun loadRecentRecords(limit: Int) {
//        if (limit == -1) {
//            compositeDisposable.add(BudgetApp.database.recordsDao().findAllRecords(System.currentTimeMillis())
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateRecentRecords(list) }, { error -> showError(error) }))
//
//        } else {
//            compositeDisposable.add(BudgetApp.database.recordsDao().findAllRecords(System.currentTimeMillis(), limit)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateRecentRecords(list) }, { error -> showError(error) }))
//
//        }
//    }
//
//    fun loadReminders(limit: Int) {
//        if (limit == -1) {
//            compositeDisposable.add(BudgetApp.database.recordsDao().findAllReminders(System.currentTimeMillis())
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateReminders(list) }, { error -> showError(error) }))
//
//        } else {
//            compositeDisposable.add(BudgetApp.database.recordsDao().findAllReminders(System.currentTimeMillis(), limit)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateReminders(list) }, { error -> showError(error) }))
//
//        }
//    }
//
//    fun loadRecordsByCategoryId(categoryId: Int, limit: Int) {
//        if (limit == -1) {
//            compositeDisposable.add(BudgetApp.database.recordsDao().findRecordsByCategoryId(categoryId)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateRecentRecords(list) }, { error -> showError(error) }))
//        }else{
//            compositeDisposable.add(BudgetApp.database.recordsDao().findRecordsByCategoryId(categoryId, limit)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ list -> recordCallback.updateRecentRecords(list) }, { error -> showError(error) }))
//        }
//    }
//
//    fun removeDashboardRecentRecord(record: Record) {
//        Single.create(SingleOnSubscribe<Int> { emitter ->
//            try {
//                val ids = BudgetApp.database.recordsDao().deleteRecord(record)
//                emitter.onSuccess(ids)
//            } catch (t: Throwable) {
//                emitter.onError(t)
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeWith(object : DisposableSingleObserver<Int>() {
//                    override fun onSuccess(ids: Int) {
//                        recordCallback.loadRecords()
//                    }
//
//                    override fun onError(e: Throwable) {
//                        showError(e)
//                    }
//                })
//    }
//
//    fun removeDashboardReminder(record: Record) {
//        record.createdFor = Date(System.currentTimeMillis())
//        record.updatedOn = Date(System.currentTimeMillis())
//        Single.create(SingleOnSubscribe<Int> { emitter ->
//            try {
//                val ids = BudgetApp.database.recordsDao().updateRecord(record)
//                emitter.onSuccess(ids)
//            } catch (t: Throwable) {
//                emitter.onError(t)
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeWith(object : DisposableSingleObserver<Int>() {
//                    override fun onSuccess(ids: Int) {
//                        recordCallback.loadReminders()
//                        recordCallback.loadRecords()
//                    }
//
//                    override fun onError(e: Throwable) {
//                        showError(e)
//                    }
//                })
//    }
//
//    fun showError(t: Throwable) {
//
//    }

    fun getIconId(catogoryId: Int): Int = recordCallback.getIconId(catogoryId)


    fun clear(){
        compositeDisposable.dispose()
    }
}