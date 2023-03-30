package com.example.myapplication.ScreenTasks

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.ScreenJobs.ListJobItem

@Composable
fun TaskItem(jobApi: JobApi, navController: NavController) {
    var job by remember { mutableStateOf<Job?>(null) }
    var listJob by remember { mutableStateOf<List<Job>>(emptyList()) }


    LaunchedEffect(true){
        listJob = jobApi.getAllJob()
    }

    LazyColumn{
        itemsIndexed(listJob){_, item ->
            ListTaskItem(item, navController)
        }
    }
}