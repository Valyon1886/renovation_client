package com.example.myapplication.ScreenJobs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.Entity.Job
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.ui.theme.Red
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenCreateJob(jobApi: JobApi, navController: NavController) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "СОЗДАНИЕ ПРОЕКТА",
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
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = BGColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Название проекта") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Описание проекта") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
//                            val jobInput = JobInput(name.value.text, description.value.text)
//                            jobApi.addTaskToJob(jobInput)
                        }
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "СОЗДАТЬ", modifier = Modifier.padding(8.dp))
                }
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