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
    fun getTitle(): String? = record.title
    fun getAmount(): String? = record.amount.toString()
    fun getTransactionDate(): String? = record.createdOn.toString()
    fun getNote(): String? = record.note
    val compositeDisposable = CompositeDisposable()

    fun setTransaction(transactionId: String, isIncome: Boolean) {
      if(transactionId.isEmpty()){
          openNewTransaction(isIncome)
      }else{
        compositeDisposable.add(getTrasactionDisposable(transactionId, isIncome))
      }
    }

    private fun openNewTransaction(isIncome: Boolean) {
        loadTransaction(Record("0", isIncome))
    }

    fun getTrasactionDisposable(transactionId: String, isIncome: Boolean): Disposable {
       return  BudgetApp.database.RecordsDao().findRecordById(transactionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({t -> loadTransaction(t)},{ error -> showError()})
    }

    private fun loadTransaction(t: Record) {
        record = t
        transactionCallback.onBindTransaction(record)
    }

    private fun showError(){
        openNewTransaction(false)
    }
}