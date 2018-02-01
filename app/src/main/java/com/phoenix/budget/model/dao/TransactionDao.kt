package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.Trasaction
import com.phoenix.budget.model.Category
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transaction ORDER BY id")
    fun getAllTransactions(): Flowable<List<Trasaction>>

    @Query("SELECT * FROM transaction where id = :id")
    fun findTransactionById(id: Int): Flowable<Trasaction>

    @Insert(onConflict = REPLACE)
    fun insertTransaction(transaction: Trasaction)

    @Update(onConflict = REPLACE)
    fun updateTransaction(transaction: Trasaction)

    @Delete
    fun deleteTransaction(transaction: Trasaction)

    @Query("DELETE FROM category")
    fun deleteAllTransaction()
}