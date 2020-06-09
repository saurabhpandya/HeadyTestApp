package com.fidato.headytestapp.dashboard.dataprovider

class CategoryRepository(private val categoryDataProvider: CategoryNetworkDataProvider) {

    suspend fun getJson() = categoryDataProvider.getJson()

}