package com.example.myapplication.Retrofit
import com.example.myapplication.Entity.*
import com.example.myapplication.Models.EmployerInput
import com.example.myapplication.Models.UserInput
import retrofit2.http.*

interface UserApi {

    @GET("user/get/token/{idToken}")
    suspend fun checkIdTokenUser(@Path("idToken") idToken: String): Boolean

    @POST("user/add")
    suspend fun addUser(@Body user: UserInput): User

    @GET("user/get/idToken/{idToken}")
    suspend fun getUserByIdToken(@Path("idToken") idToken: String): User

    @PUT("user/update/{idUser}")
    suspend fun updateUserImg(@Path("idUser") idUser: Int, @Body userImg: UserImg): User

    @GET("user/get/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("user/get/allTask/{id}")
    suspend fun getUserTask(@Path("id") id: Int): List<Job>

    @POST("user/{userId}/choise/{jobId}")
    suspend fun choiseTask(@Path("userId") userId: Int, @Path("jobId") jobId: Int): User

    @POST("user/{userId}/finish/{jobId}")
    suspend fun finishTask(@Path("userId") userId: Int, @Path("jobId") jobId: Int): User

    @DELETE("job/delete/task/{subTaskId}/{jobId}")
    suspend fun deleteTask(@Path("subTaskId") subTaskId: Int, @Path("jobId") jobId: Int)

    @GET("user/get/allMaterial/{jobId}")
    suspend fun getUserMainMaterial(@Path("jobId") jobId: Int): List<MainMaterial>

    @GET("user/get/allMaterial/{jobId}")
    suspend fun getUserMaterial(@Path("jobId") jobId: Int): List<Material>

    @POST("user/employer/add/{userId}")
    suspend fun addEmployerToUser(@Path("userId") userId: Int, @Body employer: EmployerInput): User

    @GET("user/get/allEmployer/{userId}")
    suspend fun getUserEmployerOfUser(@Path("userId") userId: Int): List<EmployerOfUser>

    @GET("user/get/allEmployer/{userId}")
    suspend fun getUserEmployer(@Path("userId") userId: Int): List<Employer>

    @DELETE("user/{userId}/deleteEmFrom/{employerId}")
    suspend fun deleteEmployerFromUser(@Path("userId") userId: Int, @Path("employerId") employerId: Int)
}