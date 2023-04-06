package com.example.myapplication.Navigation.Bottom_Navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.Entity.Job
import com.example.myapplication.ScreenDocx.ScreenDocx
import com.example.myapplication.Retrofit.EmployerApi
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.MaterialApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ScreenCreateJob
import com.example.myapplication.ScreenJobs.ScreenJobs
import com.example.myapplication.ScreenJobs.ScreenSubTask
import com.example.myapplication.ScreenMaterial.ScreenMaterial
import com.example.myapplication.ScreenProfile.ScreenProfile
import com.example.myapplication.ScreenTasks.ScreenTasks

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    jobApi: JobApi,
    materialApi: MaterialApi,
    userApi: UserApi,
    employerApi: EmployerApi
) {
    NavHost(navController = navHostController, startDestination = "tasks"){
        composable("profile"){
            ScreenProfile()
        }
        composable("jobs"){
            ScreenJobs(userApi, jobApi, navHostController)
        }
        composable("tasks"){
            ScreenTasks(userApi, jobApi, navHostController)
        }
        composable("docx"){
            ScreenDocx()
        }
        composable("${Routes.Material.route}/{id}"){ navBackStack ->
            val jobId = navBackStack.arguments?.getString("id")?.toInt() ?: 0
            ScreenMaterial(jobId = jobId, userApi, materialApi, navHostController)
        }
        composable(Routes.CreateJob.route){
            ScreenCreateJob(jobApi, navHostController)
        }
        composable("${Routes.SubTask.route}/{id}"){ navBackStack ->
            val jobId = navBackStack.arguments?.getString("id")?.toInt() ?: 0
            ScreenSubTask(jobId = jobId, jobApi, navHostController, userApi)
        }
    }
}