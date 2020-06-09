package com.fidato.headytestapp.db.repository

import android.app.Application
import android.util.Log
import com.fidato.headytestapp.db.HeadyDB
import com.fidato.headytestapp.db.dao.ProductVarientMappingDao
import com.fidato.headytestapp.db.model.ProductVarientMapping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ProductVarientMappingRepository(application: Application) : CoroutineScope {

    val TAG = ProductVarientMappingRepository::class.java.canonicalName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var productVarientMappingDao: ProductVarientMappingDao?

    init {
        val db = HeadyDB.getDatabase(application)
        productVarientMappingDao = db?.productVarientMappingDao()
    }

    fun getProductVarientMapping() = productVarientMappingDao?.getProductVarientMapping()

    suspend fun getProductVariantByProductId(productId: Int) =
        productVarientMappingDao?.getProductVarientsByProductId(productId)

    suspend fun setProductVarientMappings(arylstProductVarientMapping: List<ProductVarientMapping>) {
        for (productVarientMapping in arylstProductVarientMapping) {
            setProductVarientMappingBG(productVarientMapping)
        }
    }

    fun setProductVarientMapping(productVarientMapping: ProductVarientMapping) {
        launch {
            setProductVarientMappingBG(productVarientMapping)
        }
    }

    @Synchronized
    private suspend fun setProductVarientMappingBG(productVarientMapping: ProductVarientMapping) {

        val addedId = productVarientMappingDao?.setProductVarientMapping(productVarientMapping)
//        Log.d(TAG, "Added Id : ${addedId}");
    }

    suspend fun deleteProductVarientMapping() {
        deleteProductVarientMappingBG()
    }

    @Synchronized
    private suspend fun deleteProductVarientMappingBG() {
        withContext(Dispatchers.IO) {
            val deletedRows = productVarientMappingDao?.deleteProductVarientMapping()
            Log.d(TAG, "Deleted Rows : ${deletedRows}");
        }

    }

}