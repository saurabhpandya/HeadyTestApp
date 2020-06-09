package com.fidato.headytestapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rankings(
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    var rank_id: Int = 0,
    var ranking: String = "",
    var product_id: Int? = 0,
    var view_count: Int? = 0
)