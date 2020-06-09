package com.fidato.headytestapp.dashboard.dataprovider

import com.fidato.headytestapp.dashboard.CategoryServices

class CategoryNetworkDataProvider(private val categoryService: CategoryServices) {

    suspend fun getJson() = categoryService.getJson()

}