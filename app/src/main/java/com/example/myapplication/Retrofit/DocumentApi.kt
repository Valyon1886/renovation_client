package com.example.myapplication.Retrofit

import com.example.myapplication.Entity.Document
import com.example.myapplication.Entity.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DocumentApi {
    @POST("/api/doc/add/{jobId}")
    suspend fun generateDoc(@Body user: User, @Path("jobId") jobId: Int)
}