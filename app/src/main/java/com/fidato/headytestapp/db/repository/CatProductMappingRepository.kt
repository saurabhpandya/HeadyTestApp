package com.fidato.headytestapp.db.repository

import android.app.Application
import android.util.Log
import com.fidato.headytestapp.db.HeadyDB
import com.fidato.headytestapp.db.dao.CatProductMappingDao
import com.fidato.headytestapp.db.model.CatProductMapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CatProductMappingRepository(application: Application) : CoroutineScope {

    private val TAG = CatProductMappingRepository::class.java.canonicalName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val catProductMappingDao: CatProductMappingDao?

    init {
        val db = HeadyDB.getDatabase(application)
        catProductMappingDao = db?.catProductMappingDao()
    }

    suspend fun getProductsByRankingId(rankingId: Int) =
        catProductMappingDao?.getRankingProductsByRankingId(rankingId)

    suspend fun getCatProductMapping() = catProductMappingDao?.getCatProductMapping()

    suspend fun getProductsByCatId(catId: Int) = catProductMappingDao?.getProductsByCatId(catId)

    suspend fun setCatProductsMapping(listCatProductMapping: List<CatProductMapping>) {
        for (catProductMapping in listCatProductMapping) {
            setCatProductMappingBG(catProductMapping)
        }
    }

    fun setCatProductMapping(catProductMapping: CatProductMapping) {
        launch { setCatProductMappingBG(catProductMapping) }
    }

    @Synchronized
    private suspend fun setCatProductMappingBG(catProductMapping: CatProductMapping) {
        val addedId = catProductMappingDao?.setCatProductMapping(catProductMapping)
//        Log.d(TAG, "Added : ${addedId}")
    }

    suspend fun deleteCatProductMapping() {
        deleteCatProductMappingBG()
    }

    @Synchronized
    private suspend fun deleteCatProductMappingBG() {
        withContext(Dispatchers.IO) {
            val deletedRows = catProductMappingDao?.deleteCatProductMapping()
            Log.d(TAG, "Deleted rows : ${deletedRows}")
        }
    }

}