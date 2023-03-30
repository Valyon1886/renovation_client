package com.example.myapplication.Retrofit

import com.example.myapplication.Entity.Material
import retrofit2.http.GET
import retrofit2.http.Path

interface MaterialApi {
    @GET("material/get/{id}")
    suspend fun getMaterial(@Path("id") id: Int): Material
}