package com.example.myapplication.Retrofit
import com.example.myapplication.Entity.*
import com.example.myapplication.Models.EmployerInput
import com.example.myapplication.Models.UserInput
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("user/get/token/{idToken}")
    suspend fun checkIdTokenUser(@Path("idToken") idToken: String): Boolean

    @POST("user/add")
    suspend fun addUser(@Body user: UserInput): User

    @GET("user/get/idToken/{idToken}")
    suspend fun getUserByIdToken(@Path("idToken") idToken: String): User

    @GET("user/get/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("user/get/allTask/{id}")
    suspend fun getUserTask(@Path("id") id: Int): List<Job>

    @POST("user/{userId}/choise/{jobId}")
    suspend fun chosieTask(@Path("userId") userId: Int, @Path("jobId") jobId: Int): User

    @DELETE("job/delete/task/{subTaskId}/{jobId}")
    suspend fun deleteTask(@Path("subTaskId") subTaskId: Int, @Path("jobId") jobId: Int)

    @GET("user/get/allMaterial/{jobId}")
    suspend fun getUserMainMaterial(@Path("jobId") jobId: Int): List<MainMaterial>

    @GET("user/get/allMaterial/{jobId}")
    suspend fun getUserMaterial(@Path("jobId") jobId: Int): List<Material>

    @POST("user/employer/add/{userId}")
    suspend fun addEmployerToUser(@Path("userId") userId: Int, @Body employer: EmployerInput): User

    @GET("user/get/allEmployer/{jobId}")
    suspend fun getUserEmployerOfUser(@Path("jobId") jobId: Int): List<EmployerOfUser>

    @DELETE("user/{userId}/deleteEmFrom/{employerId}")
    suspend fun deleteEmployerFromUser(@Path("userId") userId: Int, @Path("employerId") employerId: Int)
}