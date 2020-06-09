package com.fidato.headytestapp.model

import com.fasterxml.jackson.annotation.JsonProperty

data class BaseModel(
    @JsonProperty("categories")
    val categories: ArrayList<Categories>,
    @JsonProperty("rankings")
    val rankings: ArrayList<Rankings>
)