package com.fidato.headytestapp.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidato.headytestapp.dashboard.dataprovider.CategoryNetworkDataProvider
import com.fidato.headytestapp.dashboard.dataprovider.CategoryRepository
import com.fidato.headytestapp.dashboard.viewmodel.CategoryViewModel
import com.fidato.headytestapp.product.viewmodel.ProductViewModel

class ViewModelFactory<T>(private val dataProvider: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(
                application,
                CategoryRepository(dataProvider as CategoryNetworkDataProvider)
            ) as T
        }
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}