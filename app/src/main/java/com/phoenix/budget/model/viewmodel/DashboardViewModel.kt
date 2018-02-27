package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.phoenix.budget.model.BudgetFilter
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.RecurringRecord
import com.phoenix.budget.model.RecurringRecord.RepeatType.*
import com.phoenix.budget.persistence.BudgetApp
import com.phoenix.budget.utils.Converter
import com.phoenix.budget.view.DashboardCardView
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Created by Pushpan on 12/02/18.
 */
class DashboardViewModel : ViewModel() {
    var hasInitialized = false
//    private val recentRecordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()
//    private val reminderRecordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()

    private val addRemindersResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private val updateRemindersResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private var disposable = CompositeDisposable()
    private val calendar = Calendar.getInstance()
    var recordTobeDeleted : Record? = null

    val filters = arrayListOf<BudgetFilter>()

    fun addFilter(filter: BudgetFilter){
        filters.add(filter)
    }

    fun loadData(forced: Boolean) {

        if (forced || !hasInitialized) {
            hasInitialized = true
            filters.forEach { item ->
                disposable.add(
                        item.filterFunction()
                                .doOnSubscribe { _ -> loading(item.recordsResponse) }
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { r -> item.recordsResponse.value = ModelResponse.success(r) },
                                        { throwable -> item.recordsResponse.value = ModelResponse.error(throwable) }
                                )
                )
            }
        }





//
//        if (forced || recentRecordsResponse.value == null) {
//            loadRecentRecord()
//        }
//
//        if (forced || reminderRecordsResponse.value == null) {
//            loadReminderRecordsRecord()
//        }
    }

//    private fun loadRecentRecord() {
//        disposable.add(
//                BudgetApp.database.recordsDao().findLimitRecentRecords(DashboardCardView.MAX_ROWS)
//                        .doOnSubscribe { _ -> loading(recentRecordsResponse) }
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                { r -> recentRecordsResponse.value = ModelResponse.success(r) },
//                                { throwable -> recentRecordsResponse.value = ModelResponse.error(throwable) }
//                        )
//        )
//    }

//    private fun loadReminderRecordsRecord() {
//        disposable.add(
//                BudgetApp.database.recordsDao().findLimitReminderRecords(DashboardCardView.MAX_ROWS)
//                        .doOnSubscribe { _ -> loading(reminderRecordsResponse) }
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                { r -> reminderRecordsResponse.value = ModelResponse.success(r) },
//                                { throwable -> reminderRecordsResponse.value = ModelResponse.error(throwable) }
//                        )
//        )
//    }

    fun updateReminders() {
        val timeNow = System.currentTimeMillis()
        calendar.timeInMillis = timeNow
        calendar.add(Calendar.MONTH, 2)
        val nextUpdateTime = calendar.timeInMillis

       disposable.add(Single.create(SingleOnSubscribe<ModelResponse> { emitter ->
            try {
               val recurringList = BudgetApp.database.recurringRecordsDao().findAllRecurringRecordsNeedsUpdate(timeNow)
                emitter.onSuccess(createRecordsFromReminders(recurringList, timeNow, nextUpdateTime))
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ModelResponse>() {
                    override fun onSuccess(response: ModelResponse) {
                        addRemindersResponse.value = response
                    }

                    override fun onError(e: Throwable) {
                        addRemindersResponse.value = ModelResponse.error(e)
                    }
                }))
    }

    private fun createRecordsFromReminders(list: MutableList<RecurringRecord>, timeNow: Long, nextUpdateTime: Long): ModelResponse {
        val insertList: MutableList<Record> = arrayListOf()
        list.onEach { r -> addToRecords(insertList, r, timeNow, nextUpdateTime) }

        //delete old reminders
        BudgetApp.database.recordsDao().deleteOutdatedReminders(timeNow)
        //insert new reminders records
        BudgetApp.database.recordsDao().insertRecords(insertList)
        //set next update time
        BudgetApp.database.recurringRecordsDao().updateRecurringRecord(list)
        return ModelResponse.success(insertList)
    }

    private fun addToRecords(recordList: MutableList<Record>, recurringRecord: RecurringRecord, timeNow: Long, nextUpdateTime: Long) {
        calendar.timeInMillis = recurringRecord.createdFor.time
        recurringRecord.nextUpdateOn = Date(nextUpdateTime)
        var shouldExit = false
        while (calendar.timeInMillis in timeNow..nextUpdateTime) {
            when (recurringRecord.frequency) {
                RepeatOnce -> {
                    shouldExit = true
                }
                RepeatWeekly -> {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }
                RepeatMonthly -> {
                    calendar.add(Calendar.MONTH, 1)
                }
                RepeatQuarterly -> {
                    calendar.add(Calendar.MONTH, 3)
                }
                RepeatYearly -> {
                    calendar.add(Calendar.YEAR, 1)
                }
            }

            val record = getNextRecordFor(recurringRecord, calendar.timeInMillis, nextUpdateTime)
            if (record != null) recordList.add(record) else return
            if (shouldExit) {
                return
            }

        }
    }

    private fun getNextRecordFor(recurringRecord: RecurringRecord, createFor: Long, nextUpdateTime: Long): Record? {
        if (createFor <= nextUpdateTime) {
            val record = Converter.newRecordForRecurringRecord(recurringRecord)
            record.createdFor = Date(createFor)
            record.updatedOn = Date(createFor)
            return record
        }
        return null
    }


    fun removeDashboardSingleRecord() {
        if (recordTobeDeleted != null) {
            val record = recordTobeDeleted
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
                            addRemindersResponse.value = ModelResponse.success(null)
                        }

                        override fun onError(e: Throwable) {
                            clearRecordTobeDeleted()
                            addRemindersResponse.value = ModelResponse.error(e)
                        }
                    })
        }
    }

    fun removeDashboardRecurringRecord() {
        if (recordTobeDeleted != null) {
            val record = recordTobeDeleted
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
                                addRemindersResponse.value = ModelResponse.success(success)
                            }else{
                                addRemindersResponse.value = ModelResponse.error(Exception("Something went wrong"))
                            }
                        }

                        override fun onError(e: Throwable) {
                            clearRecordTobeDeleted()
                            addRemindersResponse.value = ModelResponse.error(e)
                        }
                    })
        }
    }

    fun clearRecordTobeDeleted(){
        recordTobeDeleted = null
    }

    fun markReminderDone(record: Record) {
        record.updatedOn = Date(System.currentTimeMillis())
        record.done = true
        Single.create(SingleOnSubscribe<Int> { emitter ->
            try {
                val ids = BudgetApp.database.recordsDao().updateRecord(record)
                emitter.onSuccess(ids)
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Int>() {
                    override fun onSuccess(ids: Int) {
                        updateRemindersResponse.value = ModelResponse.success(null)
                    }
                    override fun onError(e: Throwable) {
                        updateRemindersResponse.value = ModelResponse.error(e)
                    }
                })
    }

//    fun recentRecordsResponse(): MutableLiveData<ModelResponse> = recentRecordsResponse

//    fun reminderRecordsResponse(): MutableLiveData<ModelResponse> = reminderRecordsResponse

    fun addRemindersResponse(): MutableLiveData<ModelResponse> = addRemindersResponse

    fun updateRemindersResponse(): MutableLiveData<ModelResponse> = updateRemindersResponse

    private fun loading(response: MutableLiveData<ModelResponse>) {
        response.postValue(ModelResponse.loading())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}