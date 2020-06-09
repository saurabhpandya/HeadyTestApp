package com.fidato.headytestapp.product.viewmodel

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.headytestapp.base.BaseViewModel
import com.fidato.headytestapp.dashboard.viewmodel.CategoryViewModel
import com.fidato.headytestapp.db.model.RankingProductMapping
import com.fidato.headytestapp.db.repository.CatProductMappingRepository
import com.fidato.headytestapp.db.repository.ProductVarientMappingRepository
import com.fidato.headytestapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class ProductViewModel(application: Application) : BaseViewModel(application) {

    val TAG = CategoryViewModel::class.java.canonicalName

    val catProductMappingRepo = CatProductMappingRepository(application)

    val productVariantMappingRepo = ProductVarientMappingRepository(application)

    /**
     * It will fetch Products by ranking id
     */
    fun getProductsByRankingId(rankingId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val rankingProductMappingList = catProductMappingRepo.getProductsByRankingId(rankingId)
            emit(Resource.success(rankingProductMappingList))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }

    /**
     * It will fetch Products by category id
     */
    fun getProductsByCatId(catId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {

            val arylstProductsByCat = catProductMappingRepo.getProductsByCatId(catId)
            val arylstRankingProducts = ArrayList<RankingProductMapping>()
            if (!arylstProductsByCat.isNullOrEmpty()) {
                for (catProductMapping in arylstProductsByCat) {
                    val rankingProducts = RankingProductMapping()
                    rankingProducts.products = catProductMapping
                    arylstRankingProducts.add(rankingProducts)
                }
            }
            emit(Resource.success(arylstRankingProducts))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }

    /**
     * It will fetch Products variant by product id
     */
    fun getProductVariantsByProductId(productId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val productVarientMapping =
                productVariantMappingRepo.getProductVariantByProductId(productId)

            // It will group by size so we can show color variants on tap of different size variant
            val productVarientGroupBySize = productVarientMapping?.groupBy {
                it.size
            }
            emit(Resource.success(productVarientGroupBySize))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }

}