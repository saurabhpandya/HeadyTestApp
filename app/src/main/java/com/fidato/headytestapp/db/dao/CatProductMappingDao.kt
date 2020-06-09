package com.fidato.headytestapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fidato.headytestapp.db.model.CatProductMapping
import com.fidato.headytestapp.db.model.RankingProductMapping

@Dao
interface CatProductMappingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCatProductMapping(catProductMapping: CatProductMapping): Long

    @Query("SELECT * FROM CatProductMapping ORDER BY id ASC")
    suspend fun getCatProductMapping(): List<CatProductMapping>

    @Query("SELECT * FROM CatProductMapping WHERE cat_id = :catId ORDER BY id ASC")
    suspend fun getProductsByCatId(catId: Int): List<CatProductMapping>

    @Query("DELETE FROM CatProductMapping")
    fun deleteCatProductMapping(): Int

    @Query("SELECT Rankings.*, CatProductMapping.* FROM Rankings INNER JOIN CatProductMapping ON Rankings.product_id = CatProductMapping.id WHERE Rankings.rank_id = :rankingId ORDER BY id ASC")
    suspend fun getRankingProductsByRankingId(rankingId: Int): List<RankingProductMapping>


}