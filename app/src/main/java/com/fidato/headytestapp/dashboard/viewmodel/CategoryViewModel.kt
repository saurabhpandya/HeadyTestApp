package com.fidato.headytestapp.dashboard.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.fidato.headytestapp.base.BaseViewModel
import com.fidato.headytestapp.dashboard.dataprovider.CategoryRepository
import com.fidato.headytestapp.db.model.*
import com.fidato.headytestapp.db.repository.CatProductMappingRepository
import com.fidato.headytestapp.db.repository.CategoryDBRepository
import com.fidato.headytestapp.db.repository.ProductVarientMappingRepository
import com.fidato.headytestapp.db.repository.RankingsRepository
import com.fidato.headytestapp.model.BaseModel
import com.fidato.headytestapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class CategoryViewModel(
    application: Application,
    private val dashboardRepository: CategoryRepository
) :
    BaseViewModel(application) {
    private val TAG = CategoryViewModel::class.java.canonicalName

    private val categoryRepo = CategoryDBRepository(application)
    private val catProductMappingRepo = CatProductMappingRepository(application)
    private val productVarientMappingRepo = ProductVarientMappingRepository(application)
    private val rankingsRepo = RankingsRepository(application)

    fun getCategoryByCatId(catId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            var categoryListByCatId = categoryRepo.getCategoriesByParentId(catId)
            val arylstCategoryRanking = ArrayList<CategoryRanking>()
            if (!categoryListByCatId.isNullOrEmpty()) {
                for (category in categoryListByCatId) {
                    val categoryRanking = CategoryRanking()
                    categoryRanking.id = category.catId
                    categoryRanking.name = category.name
                    categoryRanking.isCategory = true
                    arylstCategoryRanking.add(categoryRanking)
                }
            }
            emit(Resource.success(arylstCategoryRanking))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }

    fun getCategoryByParentId(parentId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val categoryByParent = categoryRepo.getCategoriesByParentId(parentId)
            val rankingGroup = rankingsRepo.getRankingGroup()
            val arylstCategoryRanking = ArrayList<CategoryRanking>()
            if (!categoryByParent.isNullOrEmpty()) {
                for (category in categoryByParent) {
                    val categoryRanking = CategoryRanking()
                    categoryRanking.id = category.catId
                    categoryRanking.name = category.name
                    categoryRanking.isCategory = true
                    arylstCategoryRanking.add(categoryRanking)
                }
            }

            if (!rankingGroup.isNullOrEmpty()) {
                for (ranking in rankingGroup) {
                    val categoryRanking = CategoryRanking()
                    categoryRanking.id = ranking.rank_id
                    categoryRanking.name = ranking.ranking
                    categoryRanking.isCategory = false
                    arylstCategoryRanking.add(categoryRanking)
                }
            }

            emit(Resource.success(arylstCategoryRanking))

        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }

    /**
     * It will delete data from tables
     */
    private suspend fun deleteDBData() {
        //    it will delete category list from local database
        categoryRepo.deleteCategory()
        //    it will delete product list from local database
        catProductMappingRepo.deleteCatProductMapping()
        //    it will delete product variant list from local database
        productVarientMappingRepo.deleteProductVarientMapping()
        //    it will delete ranking list from local database
        rankingsRepo.deleteRanking()
    }

    /**
     * It will prepare raw data to be stored in local database
     */
    private suspend fun prepareRawDataAndStoreToDB(baseModel: BaseModel) {

        // category list from base json
        val categoriesList = baseModel.categories.sortedBy { it.id }

        // ranking list from base json
        val rankingsList = baseModel.rankings

        // will be used to prepare raw data to be store in local db
        val categoriesListForDB = ArrayList<Category>()
        val catProductMappingListForDB = ArrayList<CatProductMapping>()
        val productVarientMappingListForDB = ArrayList<ProductVarientMapping>()

        // it will add category, products & product variant to arraylist
        for (category in categoriesList) {
            if (category.childCategories!!.size > 0) {
                for (childCat in category.childCategories!!) {
                    val categoryDBModel = Category()
                    categoryDBModel.catId = childCat
                    categoryDBModel.parentName = category.name
                    categoryDBModel.parentId = category.id
                    categoriesListForDB.add(categoryDBModel)
                }
                if (category.products!!.size == 0) {
                    val categoryDBModel = Category()
                    categoryDBModel.catId = category.id
                    categoryDBModel.parentName = category.name
                    categoryDBModel.parentId = 0
                    categoriesListForDB.add(categoryDBModel)
                }
            } else {
                val categoryDBModel = Category()
                categoryDBModel.catId = category.id
                categoryDBModel.name = category.name
                categoryDBModel.parentId = -1
                categoriesListForDB.add(categoryDBModel)
            }

            if (!category.products.isNullOrEmpty()) {
                // It will collect data for products of category
                for (product in category.products!!) {
                    val catProductMapping = CatProductMapping()
                    catProductMapping.id = product.id ?: 0
                    catProductMapping.name = product.name ?: ""
                    catProductMapping.date_added = product.dateAdded
                    catProductMapping.taxName = product.tax?.name ?: ""
                    catProductMapping.taxValue = product.tax?.value ?: 0.0
                    catProductMapping.cat_id = category.id
                    catProductMappingListForDB.add(catProductMapping)

                    if (!product.varients.isNullOrEmpty()) {
                        for (varient in product.varients) {
                            val productVarientMapping = ProductVarientMapping()
                            productVarientMapping.id = varient.id ?: 0
                            productVarientMapping.color = varient.color ?: ""
                            productVarientMapping.price = varient.price ?: 0.0
                            productVarientMapping.size = varient.size ?: 0.0
                            productVarientMapping.product_id = product.id ?: 0
                            productVarientMappingListForDB.add(productVarientMapping)
                        }
                    }
                }
            }
        }

        // it will group the categories by it cat id
        val groupByCatIdCatList = categoriesListForDB.groupBy {
            it.catId
        }

        val uniqueCategories = ArrayList<Category>()

        // from the grouping it will store unique categories with its parent to unique arraylist
        for ((_, categories) in groupByCatIdCatList) {
            val uniqueCategory = Category()

            for (category in categories) {
                uniqueCategory.catId = category.catId
                if (category.parentId == -1) {
                    uniqueCategory.name = category.name
                } else if (category.parentId == 0) {
                    uniqueCategory.name = category.parentName
                } else {
                    uniqueCategory.parentName = category.parentName
                    uniqueCategory.parentId = category.parentId
                }
            }
            uniqueCategories.add(uniqueCategory)
        }

        val sortedCategoriesListForDB =
            uniqueCategories
                .sortedBy { it.catId }

        Log.d(TAG, "sortedCategoriesListForDB:: ${sortedCategoriesListForDB}")

        // It will store ranking data to array by mapping with product
        val rankingListForDb = ArrayList<Rankings>()
        for ((index, ranking) in rankingsList.withIndex()) {
            if (!ranking.products.isNullOrEmpty()) {
                for (product in ranking.products) {
                    val rankingForDB = Rankings()
                    rankingForDB.rank_id = (index + 1)
                    rankingForDB.ranking = ranking.ranking
                    rankingForDB.product_id = product.id
                    rankingForDB.view_count = product.viewCount
                    rankingListForDb.add(rankingForDB)
                }
            }
        }

        // It will store all raw data to respective tables
        setRawDataToDB(
            sortedCategoriesListForDB,
            catProductMappingListForDB,
            productVarientMappingListForDB,
            rankingListForDb
        )

    }

    /**
     * It will store all raw data to respective tables
     */
    private suspend fun setRawDataToDB(
        sortedCategoriesListForDB: List<Category>,
        catProductMappingListForDB: ArrayList<CatProductMapping>,
        productVarientMappingListForDB: ArrayList<ProductVarientMapping>,
        rankingListForDb: ArrayList<Rankings>
    ) {
        // It will store unique categories to local db -> table name "Category"
        categoryRepo.setCategories(sortedCategoriesListForDB)

        // It will store products to local db -> table name "CatProductMapping"
        catProductMappingRepo.setCatProductsMapping(catProductMappingListForDB)

        // It will store product variants to local db -> table name "ProductVarientMapping"
        productVarientMappingRepo.setProductVarientMappings(productVarientMappingListForDB)

        // It will store ranking data to local db -> table name "Rankings"
        rankingsRepo.setRankings(rankingListForDb)
    }

    /**
     * It will fetch json data from network and store to local database
     * */
    fun getJson() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            // Json from network
            val baseModel = dashboardRepository.getJson()

            // It will delete data from tables
            deleteDBData()

            // It will prepare raw data to store in local database
            prepareRawDataAndStoreToDB(baseModel)

            // Updates ui once fetch and store operation completes
            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "Error Occured"))
        }
    }


}