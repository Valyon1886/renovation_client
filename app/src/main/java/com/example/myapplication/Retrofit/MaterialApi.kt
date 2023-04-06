package com.example.myapplication.Retrofit

import com.example.myapplication.Entity.Material
import com.example.myapplication.Entity.User
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Models.MaterialInput
import retrofit2.http.*

interface MaterialApi {
    @GET("material/get/{id}")
    suspend fun getMaterial(@Path("id") id: Int): Material

    @POST("material/add/toJob/{jobId}")
    suspend fun addMaterialToJob(@Body materialInput: MaterialInput, @Path("jobId") jobId: Int): Material

    @DELETE("material/delete/fromJob/{materialId}/{jobId}")
    suspend fun deleteMaterialFromJob(@Path("materialId") materialId: Int, @Path("jobId") jobId: Int)
}