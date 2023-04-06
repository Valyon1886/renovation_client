package com.example.myapplication.Retrofit


import android.telecom.Call
import android.util.Log
import com.example.myapplication.Entity.Job
import com.example.myapplication.Models.JobInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobApi {
    @GET("job/get/{id}")
    suspend fun getJob(@Path("id") id: Int): Job

    @GET("job/getAll")
    suspend fun getAllJob(): List<Job>

    @GET("job/get/allTask/{id}")
    suspend fun getAllSubTask(@Path("id") id: Int): List<Job>

    @POST("job/add")
    suspend fun createJob(@Body job: JobInput): Job

    @POST("job/add/task/{jobId}")
    suspend fun addTaskToJob(@Body job: JobInput, @Path("jobId") jobId: Int): Job

    @POST("job/{jobId}/addMatTo/{materialId}")
    suspend fun addMaterialToJob(@Path("jobId") jobId: Int, @Path("materialId") materialId: Int): Job

}