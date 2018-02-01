package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.Record
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface RecordsDao {
    @Query("SELECT * FROM records")
    fun getAllRecords(): Flowable<List<Record>>

//    @Query("SELECT * FROM transaction, category where transaction.id = :id AND transaction.category_id = category.category_id")
    @Query("SELECT * FROM records where id = :id")
    fun findRecordById(id: String): Flowable<Record>

    @Insert(onConflict = REPLACE)
    fun insertRecord(record: Record)

    @Update(onConflict = REPLACE)
    fun updateRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Query("DELETE FROM records")
    fun deleteAllRecords()
}