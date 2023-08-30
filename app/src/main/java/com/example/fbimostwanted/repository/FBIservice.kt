package com.example.fbimostwanted.repository

import com.example.fbimostwanted.models.Catalog
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FBIservice {
    @GET("list")
    fun getCatalog(@Query("page") page: Int): Call<Catalog>
}