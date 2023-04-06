package com.example.myapplication.ScreenJobs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.myapplication.Composable.JobInfo
import com.example.myapplication.Entity.Job
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenSubTask(jobId: Int, jobApi: JobApi, navController: NavController, userApi: UserApi) {
    var job by remember { mutableStateOf<Job?>(null) }
    var listSubTask by remember { mutableStateOf<List<Job>>(emptyList()) }
    LaunchedEffect(true){
        job = jobApi.getJob(jobId)
        listSubTask = jobApi.getAllSubTask(jobId)
    }
    Log.d("Screem Sub", "$jobId")
    job?.let { JobInfo(it, listSubTask, navController, jobId, jobApi, userApi) }
}