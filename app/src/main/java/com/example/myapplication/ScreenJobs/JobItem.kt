package com.example.myapplication.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ListJobItem
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.ui.theme.TextJobItem

@Composable
fun JobItem(userApi: UserApi, jobApi: JobApi, navController: NavController) {
    var job by remember { mutableStateOf<Job?>(null) }
    var user by remember { mutableStateOf<User?>(null) }
    var listJob by remember { mutableStateOf<List<Job>>(emptyList()) }


    LaunchedEffect(true){
        user = userApi.getUserById(1)
        listJob = user!!.id?.let { userApi.getUserTask(it) }!!
    }


    LazyColumn{
        itemsIndexed(listJob){_, item ->
            ListJobItem(item, navController)
        }
    }
}
