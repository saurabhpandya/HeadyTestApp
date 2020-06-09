package com.fidato.headytestapp.utils

import com.fidato.headytestapp.R

class Utility {

    companion object {
        fun getProductIcon(catId: Int, rankingName: String): Int {
            var resId = -1
            when (catId) {
                2, 4, 8 -> resId = R.drawable.ic_bottom_wear
                1, 5, 9 -> resId = R.drawable.ic_foot_wear
                6, 7, 10 -> resId = R.drawable.ic_upper_wear
                3 -> resId = R.drawable.ic_men_wear
                11 -> resId = R.drawable.ic_electornics
                12, 14, 15 -> resId = R.drawable.ic_mobile
                13, 16, 17 -> resId = R.drawable.ic_laptop
            }
            when (rankingName) {
                "Most Viewed Products" -> resId = R.drawable.ic_most_viewed
                "Most OrdeRed Products" -> resId = R.drawable.ic_most_ordered
                "Most ShaRed Products" -> resId = R.drawable.ic_most_shared
            }
            return resId
        }
    }

}