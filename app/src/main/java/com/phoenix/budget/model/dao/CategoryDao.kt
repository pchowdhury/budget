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
    @Query("SELECT * FROM category ORDER BY category_id")
    fun getAllCategory(): Flowable<List<Category>>

    @Query("SELECT * FROM category where category_id = :category_id")
    fun findCategoryByCategoryId(category_id: Int): Flowable<Category>

    @Insert(onConflict = REPLACE)
    fun insertTask(category: Category)

    @Update(onConflict = REPLACE)
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Query("DELETE FROM category")
    fun deleteAllCategories()
}