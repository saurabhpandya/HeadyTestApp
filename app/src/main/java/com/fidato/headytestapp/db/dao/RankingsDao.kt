package com.fidato.headytestapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fidato.headytestapp.db.model.Rankings


@Dao
interface RankingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setRankings(ranking: Rankings): Long

    @Query("SELECT * FROM Rankings ORDER BY rank_id ASC")
    fun getRankings(): LiveData<List<Rankings>>

    @Query("SELECT _id, rank_id, ranking FROM Rankings GROUP BY ranking ORDER BY rank_id ASC")
    suspend fun getRankingsGroup(): List<Rankings>

    @Query("DELETE FROM Rankings")
    fun deleteRankings(): Int
}