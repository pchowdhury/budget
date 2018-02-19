package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.persistence.BudgetApp
import com.phoenix.budget.view.DashboardCardView
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


/**
 * Created by Pushpan on 12/02/18.
 */
class RecordViewModel : ViewModel() {
    private val response: MutableLiveData<ModelResponse> = MutableLiveData()
    private var disposable = CompositeDisposable()


    fun loadRecordsByCategoryId(categoryId: Int) {
        if (response.value == null) {
            disposable.add(BudgetApp.database.recordsDao().findRecordsByCategoryId(categoryId)
                    .doOnSubscribe { _ -> response.postValue(ModelResponse.loading())}
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list -> response.value = ModelResponse.success(list) }, { error -> response.value = ModelResponse.error(error) }))
        }
    }

    fun loadRecentRecord() {
        if (response.value == null) {
            disposable.add(
                    BudgetApp.database.recordsDao().findRecentRecords()
                            .doOnSubscribe { _ -> response.postValue(ModelResponse.loading())}
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { r -> response.value = ModelResponse.success(r) },
                                    { throwable -> response.value = ModelResponse.error(throwable) }
                            )
            )
        }
    }

    fun response(): MutableLiveData<ModelResponse> = response

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}