package com.fidato.headytestapp.utils

interface OnItemClickListner {
    fun onItemClickListner(position: Int)
    fun onProductVariantClickListner(position: Int, isForSize: Boolean)
}