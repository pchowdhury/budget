package com.phoenix.budget.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.phoenix.budget.model.Category
import io.reactivex.Flowable

/**
 * Created by Pushpan on 28/01/18.
 */
@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id")
    fun getAllCategory(): Flowable<List<Category>>

    @Query("SELECT * FROM category where id = :id")
    fun findCategoryByCategoryId(id: Int): Flowable<Category>

    @Insert(onConflict = REPLACE)
    fun insertTask(category: Category) : Long

    @Update(onConflict = REPLACE)
    fun updateCategory(category: Category) : Int

    @Delete
    fun deleteCategory(category: Category) : Int

    @Query("DELETE FROM category")
    fun deleteAllCategories()
}