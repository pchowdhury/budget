package com.phoenix.budget.presenter

import com.phoenix.budget.ModifyRecordCallback
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.model.viewmodel.ModifyRecordViewModel
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.observers.DisposableSingleObserver


/**
 * Created by Pushpan on 26/01/18.
 */
class ModifyRecordPresenter(val viewModel: ModifyRecordViewModel, thisTransactionCallbackModify: ModifyRecordCallback) {
    var transactionCallbackModify: ModifyRecordCallback = thisTransactionCallbackModify
//    var categorizedRecord = getDefaultCategorizedRecord(false)
//    val compositeDisposable = CompositeDisposable()

    fun setCategorizedRecord(recordId: String, isIncome: Boolean, isRecurring:Boolean) {
        viewModel.setCategorizedRecord(recordId, isIncome, isRecurring)
    }
//
//    private fun openNewRecord(isIncome: Boolean) {
//        loadCategorizedRecord(getDefaultCategorizedRecord(isIncome))
//    }
//
//    fun getDefaultCategorizedRecord(isIncome: Boolean): CategorizedRecord {
//        return CategorizedRecord("email", isIncome)
//    }
//
//    fun getCategorizedRecordDisposable(recordId: String, isIncome: Boolean): Disposable {
//        return BudgetApp.database.recordsDao().findCategorizedRecordById(recordId)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ t -> loadCategorizedRecord(t) }, { _ -> showError() })
//    }
//
//    private fun loadCategorizedRecord(t: CategorizedRecord) {
//        categorizedRecord = t
//        transactionCallbackModify.onBindRecord(categorizedRecord)
//    }

//    private fun showError() {
//        openNewRecord(false)
//    }
//
//    fun cleanUp() {
//        compositeDisposable.clear()
//    }
//
//    fun save() {
//        compositeDisposable.add(getInsertRecordDisposable())
//    }
//
//    fun getInsertRecordDisposable(): Disposable {
//        return Single.create(SingleOnSubscribe<Long> { emitter ->
//            try {
//                val ids = BudgetApp.database.recordsDao().insertRecord(categorizedRecord.getRecurringRecord())
//                emitter.onSuccess(ids)
//            } catch (t: Throwable) {
//                emitter.onError(t)
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeWith(object : DisposableSingleObserver<Long>() {
//                    override fun onSuccess(ids: Long) {
//                        transactionCallbackModify.closeRecord()
//                    }
//
//                    override fun onError(e: Throwable) {
//                        showError()
//                    }
//                })
//    }

//    fun getInsertCategoryDisposable(recordId: String, isIncome: Boolean): Disposable {
//        return Single.create(SingleOnSubscribe<Int> { emitter ->
//            try {
//                val ids = BudgetApp.database.recordsDao().insertRecord(categorizedRecord.getRecord())
//                emitter.onSuccess(ids)
//            } catch (t: Throwable) {
//                emitter.onError(t)
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribeWith(object : DisposableSingleObserver<Int>() {
//                    override fun onSuccess(ids: Int) {
//
//                    }
//                    override fun onError(e: Throwable) {
//                        showError()
//                    }
//                })
//    }
}