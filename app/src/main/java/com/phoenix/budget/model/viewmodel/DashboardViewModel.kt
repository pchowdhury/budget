package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.RecurringRecord
import com.phoenix.budget.model.RecurringRecord.RepeatType.*
import com.phoenix.budget.persistence.BudgetApp
import com.phoenix.budget.utils.Converter
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Created by Pushpan on 12/02/18.
 */
class DashboardViewModel : ReportViewModel() {
    private val addRemindersResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private val updateRemindersResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private val calendar = Calendar.getInstance()


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

    fun addRemindersResponse(): MutableLiveData<ModelResponse> = addRemindersResponse

    fun updateRemindersResponse(): MutableLiveData<ModelResponse> = updateRemindersResponse

}