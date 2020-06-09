package com.fidato.headytestapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Rankings(
    @JsonProperty("ranking")
    val ranking: String = "",
    @JsonProperty("products")
    val products: ArrayList<RankingProducts>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RankingProducts(
    @JsonProperty("id")
    val id: Int = 0,
    @JsonProperty("view_count")
    val viewCount: Int = 0
)