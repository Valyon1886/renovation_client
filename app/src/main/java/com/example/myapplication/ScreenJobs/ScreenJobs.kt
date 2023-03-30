package com.example.myapplication.ScreenJobs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Composable.JobItem
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.GrayMain
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenJobs(userApi: UserApi, jobApi: JobApi, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "ПРОЕКТЫ",
                        color = Color.White
                    )
                },
                backgroundColor = NavColor
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
            ) {
                JobItem(userApi, jobApi, navController)
            }
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Text("Bottom App Bar")
            }
        }
    )
}