package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.observers.DisposableSingleObserver


/**
 * Created by Pushpan on 12/02/18.
 */
class ModifyRecordViewModel : ViewModel() {
    private var isRecurring = false
    private var isIncome: Boolean = false
    private var id: String = ""
    private val response: MutableLiveData<ModelResponse> = MutableLiveData()
    private var disposable = CompositeDisposable()
    private var saved: Boolean = false

    private fun loadRecord() {
        if (isRecurring) {
            disposable.add(
                    BudgetApp.database.recurringRecordsDao().findCategorizedRecordById(id)
                            .doOnSubscribe { _ -> loading() }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { r -> success(r) },
                                    { throwable -> error((throwable)) }
                            )
            )
        } else {
            disposable.add(
                    BudgetApp.database.recordsDao().findCategorizedRecordById(id)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { r -> success(r) },
                                    { throwable -> error((throwable)) }
                            )
            )
        }
    }

    fun response(): MutableLiveData<ModelResponse> = response

    fun editableCategorizedRecord(): CategorizedRecord = response.value!!.value as CategorizedRecord

    private fun loading() {
        response.value = ModelResponse.loading()
    }

    private fun success(value: Any?) {
        response.value = ModelResponse.success(value)
    }

    private fun error(error: Throwable?) {
        response.value = ModelResponse.error(error)
    }

    fun setCategorizedRecord(recordId: String, isIncome: Boolean, isRecurring: Boolean) {
        this.isIncome = isIncome
        this.isRecurring = isRecurring
        if (recordId.isEmpty()) {
            openNewRecord()
        } else {
            loadRecord()
        }
    }

    fun openNewRecord() {
        response.value = ModelResponse.success(CategorizedRecord("email", isIncome))
    }

    fun save(addAnother: Boolean) {
        disposable.add(getInsertRecordDisposable(addAnother))
    }


    fun getInsertRecordDisposable(addAnother: Boolean): Disposable {
        if (isRecurring) {
            return Single.create(SingleOnSubscribe<Long> { emitter ->
                try {
                    val ids = BudgetApp.database.recurringRecordsDao().insertRecurringRecord((response().value!!.value as CategorizedRecord).getRecurringRecord())
                    emitter.onSuccess(ids)
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Long>() {
                        override fun onSuccess(ids: Long) {
                            response.value = ModelResponse.action(if (addAnother) ModelResponse.ACTION_2 else ModelResponse.ACTION_1)
                            saved = true
                        }

                        override fun onError(e: Throwable) {
                            response.value = ModelResponse.error(e)
                        }
                    })
        } else {
            return Single.create(SingleOnSubscribe<Long> { emitter ->
                try {
                    val ids = BudgetApp.database.recordsDao().insertRecord((response().value!!.value as CategorizedRecord).getRecord())
                    emitter.onSuccess(ids)
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Long>() {
                        override fun onSuccess(ids: Long) {
                            response.value = ModelResponse.action(if (addAnother) ModelResponse.ACTION_2 else ModelResponse.ACTION_1)
                            saved = true
                        }

                        override fun onError(e: Throwable) {
                            response.value = ModelResponse.error(e)
                        }
                    })
        }
    }

//    fun getDefaultCategorizedRecord(isIncome: Boolean): CategorizedRecord {
//        return CategorizedRecord("email", isIncome)
//    }
//
//    private fun loadCategorizedRecord(t: CategorizedRecord) {
//        categorizedRecord = t
//        transactionCallbackModify.onBindRecord(categorizedRecord)
//    }


//    private fun showError() {
//        recurringRecord?
//    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}