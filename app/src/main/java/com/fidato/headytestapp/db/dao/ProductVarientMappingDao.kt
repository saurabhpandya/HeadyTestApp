package com.fidato.headytestapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fidato.headytestapp.db.model.ProductVarientMapping

@Dao
interface ProductVarientMappingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setProductVarientMapping(productVarientMapping: ProductVarientMapping): Long

    @Query("SELECT * FROM ProductVarientMapping ORDER BY id ASC")
    fun getProductVarientMapping(): LiveData<List<ProductVarientMapping>>

    @Query("SELECT * FROM ProductVarientMapping WHERE product_id = :productId ORDER BY id ASC")
    suspend fun getProductVarientsByProductId(productId: Int): List<ProductVarientMapping>

    @Query("DELETE FROM ProductVarientMapping")
    fun deleteProductVarientMapping(): Int
}