package com.fidato.headytestapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fidato.headytestapp.db.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCategory(category: Category): Long

    @Query("SELECT * FROM Category ORDER BY catId ASC")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM Category WHERE catId = :catId ORDER BY catId ASC")
    suspend fun getCategoriesByCatId(catId: Int): List<Category>

    @Query("SELECT * FROM Category WHERE parentId = :parentId ORDER BY catId ASC")
    suspend fun getCategoriesByParentId(parentId: Int): List<Category>

    @Query("DELETE FROM Category")
    fun deleteCategories(): Int

}