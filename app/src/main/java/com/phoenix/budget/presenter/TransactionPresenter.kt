package com.phoenix.budget.presenter

import com.phoenix.budget.TransactionCallback
import com.phoenix.budget.model.CategorizedRecord
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
    lateinit var categorizedRecord: CategorizedRecord
    val compositeDisposable = CompositeDisposable()

    fun setCategorizedRecord(recordId: String, isIncome: Boolean) {
      if(recordId.isEmpty()){
          openNewRecord(isIncome)
      }else{
        compositeDisposable.add(getCategorizedRecordDisposable(recordId, isIncome))
      }
    }

    private fun openNewRecord(isIncome: Boolean) {
        loadCategorizedRecord(CategorizedRecord("email", isIncome))
    }

    fun getCategorizedRecordDisposable(recordId: String, isIncome: Boolean): Disposable {
       return  BudgetApp.database.RecordsDao().findCategorizedRecordById(recordId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({t -> loadCategorizedRecord(t)},{ error -> showError()})
    }

    private fun loadCategorizedRecord(t: CategorizedRecord) {
        categorizedRecord = t
        transactionCallback.onBindRecord(categorizedRecord)
    }

    private fun showError(){
        openNewRecord(false)
    }

    fun cleanUp(){
        compositeDisposable.clear()
    }
}