package com.fidato.headytestapp.dashboard

import com.fidato.headytestapp.model.BaseModel
import retrofit2.http.GET

interface CategoryServices {

    @GET("json")
    suspend fun getJson(): BaseModel

}