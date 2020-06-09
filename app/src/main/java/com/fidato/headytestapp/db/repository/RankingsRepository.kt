package com.fidato.headytestapp.db.repository

import android.app.Application
import android.util.Log
import com.fidato.headytestapp.db.HeadyDB
import com.fidato.headytestapp.db.dao.RankingsDao
import com.fidato.headytestapp.db.model.Rankings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RankingsRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val TAG = RankingsRepository::class.java.canonicalName

    private var rankingsDao: RankingsDao?

    init {
        val db = HeadyDB.getDatabase(application)
        rankingsDao = db?.rankingsDao();
    }

    fun getRanking() = rankingsDao?.getRankings()

    suspend fun getRankingGroup() = rankingsDao?.getRankingsGroup()

    suspend fun setRankings(arylstRanking: List<Rankings>) {
        for (ranking in arylstRanking) {
            setRankingBG(ranking)
        }
    }

    fun setRanking(ranking: Rankings) {
        launch {
            setRankingBG(ranking)
        }
    }

    @Synchronized
    private suspend fun setRankingBG(ranking: Rankings) {
        withContext(Dispatchers.IO) {
            val addedId = rankingsDao?.setRankings(ranking)
//            Log.d(TAG, "Added Id : ${addedId}");
        }
    }

    suspend fun deleteRanking() {
        deleteRankingBG()
    }

    @Synchronized
    private suspend fun deleteRankingBG() {
        withContext(Dispatchers.IO) {
            val deletedRows = rankingsDao?.deleteRankings()
            Log.d(TAG, "Deleted Rows : ${deletedRows}");
        }

    }
}