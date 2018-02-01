package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.Transaction
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transaction ORDER BY iid")
    fun getAllTransactions(): Flowable<List<Transaction>>

//    @Query("SELECT * FROM transaction, category where transaction.id = :id AND transaction.category_id = category.category_id")
    @Query("SELECT * FROM transaction where id = :id")
    fun findTransactionById(id: String): Flowable<Transaction>

    @Insert(onConflict = REPLACE)
    fun insertTransaction(transaction: Transaction)

    @Update(onConflict = REPLACE)
    fun updateTransaction(transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM category")
    fun deleteAllTransaction()
}