package com.example.myapplication.Retrofit

import com.example.myapplication.Entity.Document
import com.example.myapplication.Entity.User
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface DocumentApi {
    @POST("/api/doc/add/{jobId}")
    suspend fun generateDoc(@Body user: User, @Path("jobId") jobId: Int)
    @GET("/api/doc/getAllFileNames/{userId}")
    suspend fun getAllFileNames(@Path("userId") userId: Int): MutableList<String>
    @GET("/api/doc/getFile/{fileName}")
    @Streaming
    suspend fun downloadFile(@Path("fileName") fileName: String): Response<ResponseBody>
}