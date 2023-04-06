package com.example.myapplication.Retrofit
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.Material
import com.example.myapplication.Entity.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("user/get/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("user/get/allTask/{id}")
    suspend fun getUserTask(@Path("id") id: Long): List<Job>

    @POST("user/{userId}/choise/{jobId}")
    suspend fun chosieTask(@Path("userId") userId: Int, @Path("jobId") jobId: Int): User

    @DELETE("job/delete/task/{subTaskId}/{jobId}")
    suspend fun deleteTask(@Path("subTaskId") subTaskId: Int, @Path("jobId") jobId: Int)

    @GET("user/get/allMaterial/{jobId}")
    suspend fun getUserMaterial(@Path("jobId") jobId: Int): List<Material>
}