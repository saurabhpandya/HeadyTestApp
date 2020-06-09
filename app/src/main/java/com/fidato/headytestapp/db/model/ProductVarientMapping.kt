package com.fidato.headytestapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductVarientMapping(

    @PrimaryKey
    var id: Int = 0,
    var product_id: Int = 0,
    var color: String = "",
    var size: Double = 0.0,
    var price: Double = 0.0
)