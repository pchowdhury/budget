package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.phoenix.budget.persistence.BudgetApp
import com.phoenix.budget.view.DashboardCardView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Pushpan on 12/02/18.
 */
class DashboardViewModel : ViewModel() {
    private var shouldLoadData = true
    private val recentRecordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private val reminderRecordsResponse: MutableLiveData<ModelResponse> = MutableLiveData()
    private var disposable = CompositeDisposable()

    fun loadData(forced: Boolean) {
        if (shouldLoadData || forced) {
            loadRecentRecord()
            loadReminderRecordsRecord()
            shouldLoadData = false
        }
    }

    private fun loadRecentRecord() {
        disposable.add(
                BudgetApp.database.recordsDao().findLimitedRecords(false, DashboardCardView.MAX_ROWS)
                        .doOnSubscribe { _ -> loading(recentRecordsResponse) }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { r -> recentRecordsResponse.value = ModelResponse.success(r) },
                                { throwable -> recentRecordsResponse.value = ModelResponse.error(throwable) }
                        )
        )
    }

    private fun loadReminderRecordsRecord() {
        disposable.add(
                BudgetApp.database.recordsDao().findLimitedRecords(true, DashboardCardView.MAX_ROWS)
                        .doOnSubscribe { _ -> loading(reminderRecordsResponse) }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { r -> reminderRecordsResponse.value = ModelResponse.success(r) },
                                { throwable -> reminderRecordsResponse.value = ModelResponse.error(throwable) }
                        )
        )
    }

    fun recentRecordsResponse(): MutableLiveData<ModelResponse> = recentRecordsResponse

    fun reminderRecordsResponse(): MutableLiveData<ModelResponse> = reminderRecordsResponse


    private fun loading(response: MutableLiveData<ModelResponse>) {
        response.postValue(ModelResponse.loading())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}