package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.model.Record
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface RecordsDao {
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT records.*, category.id as category_id, category.title as category_title, category.created_on as category_created_on, category.updated_on as category_updated_on FROM records LEFT JOIN category ON records.category_id = category.id AND records.id = :id")
    fun findCategorizedRecordById(id: String): Flowable<CategorizedRecord>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records ORDER BY created_for DESC")
    fun findAllRecords(): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE done = 1 ORDER BY updated_on DESC")
    fun findRecentRecords(): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE done = 0 ORDER BY created_for ASC")
    fun findReminderRecords(): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE done = 1 ORDER BY updated_on DESC LIMIT :limit")
    fun findLimitRecentRecords(limit: Int): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE done = 0 ORDER BY created_for ASC LIMIT :limit")
    fun findLimitReminderRecords(limit: Int): Flowable<MutableList<Record>>

//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM records WHERE done = :isReminder ORDER BY created_for DESC LIMIT :limit")
//    fun findLimitedRecords(isReminder:String, limit:Int): Flowable<MutableList<Record>>
//
//



    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE created_for >  :time ORDER BY created_for ASC")
    fun findAllReminders(time:Long): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE created_for >  :time ORDER BY created_for ASC LIMIT :limit")
    fun findAllReminders(time:Long, limit:Int): Flowable<MutableList<Record>>

    //    @Query("SELECT * FROM transaction, category where transaction.id = :id AND transaction.category_id = category.category_id")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records where id = :id")
    fun findRecordById(id: String): Flowable<Record>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT records.*, category.id as category_id, category.title as category_title, category.created_on as category_created_for, category.updated_on as category_updated_on FROM records LEFT JOIN category ON records.category_id = category.id")
    fun findCategorizedRecords(): Flowable<CategorizedRecord>



    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE records.category_id = :id AND records.is_income = 0 LIMIT :limit")
    fun findRecordsByCategoryId(id: Int, limit:Int): Flowable<MutableList<Record>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM records WHERE records.category_id = :id AND records.is_income = 0")
    fun findRecordsByCategoryId(id: Int): Flowable<MutableList<Record>>

    @Insert(onConflict = REPLACE)
    fun insertRecord(record: Record) : Long

    @Insert(onConflict = REPLACE)
    fun insertRecords(records: MutableList<Record>) : LongArray

    @Update(onConflict = REPLACE)
    fun updateRecords(records: MutableList<Record>): Int

    @Update(onConflict = REPLACE)
    fun updateRecord(record: Record): Int

    @Delete
    fun deleteRecord(record: Record?): Int

    @Query("DELETE FROM records")
    fun deleteAllRecords()

    @Query("DELETE FROM records WHERE done = 0 AND associated_id = :id")
    fun deleteAllRecordsWithAssociatedId(id: String)

    @Query("DELETE FROM records WHERE done = 0 AND (associated_id IN (SELECT id from recurring_records WHERE next_update_on <= :time))")
    fun deleteOutdatedReminders(time:Long)
}