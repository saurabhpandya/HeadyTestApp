package com.fidato.headytestapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Product(
    @JsonProperty("id")
    val id: Int?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("date_added")
    val dateAdded: Date?,
    @JsonProperty("variants")
    val varients: ArrayList<Varients>?,
    @JsonProperty("tax")
    val tax: Tax?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Varients(
    @JsonProperty("id")
    val id: Int?,
    @JsonProperty("color")
    val color: String?,
    @JsonProperty("size")
    val size: Double?,
    @JsonProperty("price")
    val price: Double?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tax(
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("value")
    val value: Double?
)