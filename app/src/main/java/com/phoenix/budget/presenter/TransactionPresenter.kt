package com.phoenix.budget.presenter

import com.phoenix.budget.TransactionCallback
import com.phoenix.budget.model.Transaction
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
    lateinit var transaction: Transaction
    fun getTitle(): String? = transaction.title
    fun getAmount(): String? = transaction.amount.toString()
    fun getTransactionDate(): String? = transaction.createdOn.toString()
    fun getNote(): String? = transaction.note
    val compositeDisposable = CompositeDisposable()

    fun setTransaction(transactionId: String, isIncome: Boolean) {
      if(transactionId.isBlank()){
          openNewTransaction(isIncome)
      }else{
        compositeDisposable.add(getTrasactionDisposable(transactionId, isIncome))
      }
    }

    private fun openNewTransaction(isIncome: Boolean) {
        loadTransaction(Transaction("0", isIncome))
    }

    fun getTrasactionDisposable(transactionId: String, isIncome: Boolean): Disposable {
       return  BudgetApp.database.transactionDao().findTransactionById(transactionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({t -> loadTransaction(t)},{ error -> showError()})
    }

    private fun loadTransaction(t: Transaction) {
        transaction = t
        transactionCallback.onBindTransaction(transaction)
    }

    private fun showError(){
        openNewTransaction(false)
    }
}