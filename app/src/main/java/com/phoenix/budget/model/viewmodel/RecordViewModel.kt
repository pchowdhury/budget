package com.phoenix.budget.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
class RecordViewModel : ViewModel() {
    private val response: MutableLiveData<ModelResponse> = MutableLiveData()
    private var disposable = CompositeDisposable()
    private var categoryId: Int = -1

    fun loadRecords(categoryId: Int, forceLoad: Boolean){
        this.categoryId = categoryId
        if (categoryId == -1) {
            loadRecentRecord(forceLoad)
        } else {
            loadRecordsByCategoryId(categoryId, forceLoad)
        }

    }

    fun loadRecordsByCategoryId(categoryId: Int, forceLoad: Boolean) {
        if (response.value == null || forceLoad) {
            disposable.add(BudgetApp.database.recordsDao().findRecordsByCategoryId(categoryId)
                    .doOnSubscribe { _ -> response.postValue(ModelResponse.loading())}
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(3)
                    .subscribe({ list -> response.value = ModelResponse.success(list) }, { error -> response.value = ModelResponse.error(error) }))
        }
    }

    fun loadRecentRecord(forceLoad: Boolean) {
        if (response.value == null || forceLoad) {
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

    fun removeSingleRecord(record: Record) {
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
                            loadRecentRecord(true)
                        }

                        override fun onError(e: Throwable) {

                        }
                    })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}