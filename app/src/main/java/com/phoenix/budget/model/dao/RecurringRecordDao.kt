package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.RecurringRecord
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface RecurringRecordDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT recurring_records.*, category.id as category_id, category.title as category_title, category.created_on as category_created_on, category.updated_on as category_updated_on FROM recurring_records LEFT JOIN category ON recurring_records.category_id = category.id AND recurring_records.id = :id")
    fun findCategorizedRecordById(id: String): Flowable<CategorizedRecord>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM recurring_records ORDER BY created_for ASC")
    fun findAllRecurringRecords(): Flowable<MutableList<RecurringRecord>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE done = 1 ORDER BY created_for ASC LIMIT :limit")
    fun findLimitedRecurringRecords(limit:Int): Flowable<MutableList<RecurringRecord>>
//
//    //    @Query("SELECT * FROM transaction, category where transaction.id = :id AND transaction.category_id = category.category_id")
//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM recurring_records where id = :id")
//    fun findRecurringRecordById(id: String): Flowable< RecurringRecord>

//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT records.*, category.id as category_id, category.title as category_title, category.created_for as category_created_for, category.updated_on as category_updated_on FROM records LEFT JOIN category ON records.category_id = category.id")
//    fun findCategorizedRecords(): Flowable<CategorizedRecord>
//
//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT records.*, category.id as category_id, category.title as category_title, category.created_for as category_created_for, category.updated_on as category_updated_on FROM records LEFT JOIN category ON records.category_id = category.id AND records.id = :id")
//    fun findCategorizedRecordById(id: String): Flowable<CategorizedRecord>
//
//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM records WHERE records.category_id = :id AND records.is_income = 0 LIMIT :limit")
//    fun findRecordsByCategoryId(id: Int, limit:Int): Flowable<MutableList<Record>>
//
//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM records WHERE records.category_id = :id AND records.is_income = 0")
//    fun findRecordsByCategoryId(id: Int): Flowable<MutableList<Record>>

    @Insert(onConflict = REPLACE)
    fun insertRecurringRecord(record: RecurringRecord) : Long

    @Update(onConflict = REPLACE)
    fun updateRecurringRecord(record: RecurringRecord): Int

    @Delete
    fun deleteRecurringRecord(record: RecurringRecord): Int

    @Query("DELETE FROM recurring_records")
    fun deleteAllRecurringRecords()
}