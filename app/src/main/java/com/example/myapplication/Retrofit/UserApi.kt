package com.example.myapplication.Retrofit
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("user/get/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("user/get/allTask/{id}")
    suspend fun getUserTask(@Path("id") id: Long): List<Job>
}