package com.fidato.headytestapp.db.model

import androidx.room.Embedded
import java.io.Serializable

data class RankingProductMapping(

    @Embedded
    var ranking: Rankings? = null,

    @Embedded
    var products: CatProductMapping? = null

): Serializable