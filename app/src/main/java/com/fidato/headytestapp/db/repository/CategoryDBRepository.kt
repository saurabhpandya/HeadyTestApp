package com.fidato.headytestapp.db.repository

import android.app.Application
import android.util.Log
import com.fidato.headytestapp.db.HeadyDB
import com.fidato.headytestapp.db.dao.CategoryDao
import com.fidato.headytestapp.db.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CategoryDBRepository(application: Application) : CoroutineScope {

    private val TAG = CategoryDBRepository::class.java.canonicalName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var categoryDao: CategoryDao?

    init {
        val db = HeadyDB.getDatabase(application)
        categoryDao = db?.categoryDao()
    }

    fun getCategory() = categoryDao?.getCategories()

    suspend fun getCategoriesByCatId(catId: Int) =
        categoryDao?.getCategoriesByCatId(catId)

    suspend fun getCategoriesByParentId(parentId: Int) =
        categoryDao?.getCategoriesByParentId(parentId)

    suspend fun setCategories(arylstCategory: List<Category>) {
        for (category in arylstCategory) {
            setCategoryBG(category)
        }
    }

    fun setCategory(category: Category) {
        launch {
            setCategoryBG(category)
        }
    }

    @Synchronized
    private suspend fun setCategoryBG(category: Category) {
        withContext(Dispatchers.IO) {
            val addedId = categoryDao?.setCategory(category)
//            Log.d(TAG, "Added Id : ${addedId}");
        }
    }

    suspend fun deleteCategory() {
        deleteCategoryBG()
    }

    @Synchronized
    private suspend fun deleteCategoryBG() {
        withContext(Dispatchers.IO) {
            val deletedRows = categoryDao?.deleteCategories()
            Log.d(TAG, "Deleted Rows : ${deletedRows}")
        }

    }

}