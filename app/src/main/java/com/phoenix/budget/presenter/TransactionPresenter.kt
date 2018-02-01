package com.phoenix.budget.presenter

import com.phoenix.budget.TransactionCallback
import com.phoenix.budget.model.Record
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Pushpan on 26/01/18.
 */
class TransactionPresenter(thisTransactionCallback: TransactionCallback) {
    var transactionCallback: TransactionCallback = thisTransactionCallback
    lateinit var record: Record
    val compositeDisposable = CompositeDisposable()

    fun setRecord(recordId: String, isIncome: Boolean) {
      if(recordId.isEmpty()){
          openNewRecord(isIncome)
      }else{
        compositeDisposable.add(getTrasactionDisposable(recordId, isIncome))
      }
    }

    private fun openNewRecord(isIncome: Boolean) {
        loadRecord(Record("0", isIncome))
    }

    fun getTrasactionDisposable(transactionId: String, isIncome: Boolean): Disposable {
       return  BudgetApp.database.RecordsDao().findRecordById(transactionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({t -> loadRecord(t)},{ error -> showError()})
    }

    private fun loadRecord(t: Record) {
        record = t
        transactionCallback.onBindRecord(record)
    }

    private fun showError(){
        openNewRecord(false)
    }

    fun cleanUp(){
        compositeDisposable.clear()
    }
}