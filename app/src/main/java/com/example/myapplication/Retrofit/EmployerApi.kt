package com.example.myapplication.Retrofit

import com.example.myapplication.Entity.Employer
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployerApi {
    @GET("employer/get/{id}")
    suspend fun getEmployer(@Path("id") id: Int): Employer
}