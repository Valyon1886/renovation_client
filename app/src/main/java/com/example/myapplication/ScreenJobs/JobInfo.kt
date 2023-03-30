package com.example.myapplication.Composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.R
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.ScreenJobs.ListSubTask
import com.example.myapplication.ScreenJobs.ScreenSubTask
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun JobInfo(job: Job, listTask: List<Job>, navController: NavController, jobId: Int, jobApi: JobApi){
    var showDialog by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }

    var endList = remember { mutableStateOf(listTask) }

    LaunchedEffect(true){
        endList.value = jobApi.getAllSubTask(jobId)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ПЛАН",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Material.route) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Материалы", tint = Color.White)
                    }
                },
                backgroundColor = NavColor
            )
        },
        content = {
            job.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BGColor),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        backgroundColor = NavColor
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                text = "${it.name}"
                            )
                            Text(text = "Название", color = TextJobItem, fontSize = 12.sp)
                            Text(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color.White,
                                text = "${it.description}"
                            )
                        }
                    }
                    LazyColumn{
                        itemsIndexed(endList.value){_, item ->
                            ListSubTask(item, navController)
                        }
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(text = "Добавить подзадачу") },
                            text = {
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
                                }
                            },
                            buttons = {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    TextButton(
                                        onClick = { showDialog = false },
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text("Отмена")
                                    }
                                    Button(
                                        onClick = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                val jobInput = JobInput(name.value.text, description.value.text)
                                                jobApi.addTaskToJob(jobInput, jobId)

                                                endList.value = jobApi.getAllSubTask(jobId)
                                            }
                                            showDialog = false

                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(text = "СОЗДАТЬ", modifier = Modifier.padding(8.dp))
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                backgroundColor = Red,
                onClick = { showDialog = true },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "fab icon", tint = Color.White)
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
