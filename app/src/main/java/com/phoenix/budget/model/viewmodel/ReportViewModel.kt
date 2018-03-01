package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.phoenix.budget.model.BudgetFilter
import com.phoenix.budget.model.Record
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


/**
 * Created by Pushpan on 12/02/18.
 */
open class ReportViewModel : ViewModel() {
    var hasInitialized = false
    var disposable = CompositeDisposable()
    var recordTobeDeleted : Record? = null
    var currentBudgetFilter : BudgetFilter? = null

    val filters = arrayListOf<BudgetFilter>()

    fun addFilter(filter: BudgetFilter){
        filters.add(filter)
    }

    fun loadFilters(forced: Boolean) {
        if (forced || !hasInitialized) {
            hasInitialized = true
            filters.forEach { item ->
                loadFilter(item)
            }
        }
    }


    private fun loadFilter(filter: BudgetFilter?) {
        if (filter != null) {
            disposable.add(
                    filter.filterFunction()
                            .doOnSubscribe { _ -> loading(filter.recordsResponse) }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { r -> filter.recordsResponse.value = ModelResponse.success(r) },
                                    { throwable -> filter.recordsResponse.value = ModelResponse.error(throwable) }
                            )
            )
        }
    }

    fun budgetFilterFor(filterName: String): BudgetFilter = filters.filter { t -> t.name == filterName }.single()

    fun markItemToBeDeleted(filter: String, record: Record) {
        currentBudgetFilter = budgetFilterFor(filter)
        recordTobeDeleted = record
    }

    fun removeDashboardSingleRecord() {
        if (currentBudgetFilter!=null && recordTobeDeleted != null) {
            val record = recordTobeDeleted
            val filter = currentBudgetFilter
            Single.create(SingleOnSubscribe<Int> { emitter ->
                try {
                    val ids = BudgetApp.database.recordsDao().deleteRecord(record)
                    emitter.onSuccess(ids)
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Int>() {
                        override fun onSuccess(ids: Int) {
                            clearRecordTobeDeleted()
                            loadFilter(filter)
                        }

                        override fun onError(e: Throwable) {
                            clearRecordTobeDeleted()
                            loadFilter(filter)
                        }
                    })
        }
    }

    fun removeDashboardRecurringRecord() {
        if (currentBudgetFilter!=null && recordTobeDeleted != null) {
            val record = recordTobeDeleted
            val filter = currentBudgetFilter
            Single.create(SingleOnSubscribe<Boolean> { emitter ->
                try {
                    if (record != null) {
                        BudgetApp.database.recurringRecordsDao().deleteRecurringRecordWithId(record.associatedId)
                        BudgetApp.database.recordsDao().deleteAllRecordsWithAssociatedId(record.associatedId)
                        emitter.onSuccess(true)
                    }else{
                        emitter.onSuccess(false)
                    }
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<Boolean>() {
                        override fun onSuccess(success: Boolean) {
                            clearRecordTobeDeleted()
                            if(success){
//                                addRemindersResponse.value = ModelResponse.success(success)
                            }else{
//                                addRemindersResponse.value = ModelResponse.error(Exception("Something went wrong"))
                            }
                            loadFilter(filter)
                        }

                        override fun onError(e: Throwable) {
                            clearRecordTobeDeleted()
                            loadFilter(filter)
//                            addRemindersResponse.value = ModelResponse.error(e)
                        }
                    })
        }
    }

    fun clearRecordTobeDeleted(){
        recordTobeDeleted = null
        currentBudgetFilter = null
    }


    private fun loading(response: MutableLiveData<ModelResponse>) {
        response.postValue(ModelResponse.loading())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}