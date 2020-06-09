package com.fidato.headytestapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Categories(
    @JsonProperty("id")
    var id: Int = 0,
    @JsonProperty("name")
    var name: String = "",
    @JsonProperty("products")
    var products: ArrayList<Product>? = null,
    @JsonProperty("child_categories")
    var childCategories: ArrayList<Int>? = null
)