package com.example.myapplication.Navigation.Bottom_Navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Retrofit.EmployerApi
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.MaterialApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.NavColor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8081").client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi = retrofit.create(UserApi::class.java)
    val materialApi = retrofit.create(MaterialApi::class.java)
    val employerApi = retrofit.create(EmployerApi::class.java)
    val jobApi = retrofit.create(JobApi::class.java)

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {
        NavGraph(navHostController = navController, jobApi, materialApi, userApi, employerApi)
    }
}