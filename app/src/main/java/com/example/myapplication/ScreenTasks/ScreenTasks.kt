package com.example.myapplication.ScreenTasks

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Auth.RoutesAuth
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.R
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenTasks(userApi: UserApi, jobApi: JobApi, navController: NavController, user: User) {

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    var rotate = remember { mutableStateOf(0) }
    var label = remember { mutableStateOf(R.drawable.baseline_trending_down_24) }

    var job by remember { mutableStateOf<Job?>(null) }
    var listJob by remember { mutableStateOf<List<Job>>(emptyList()) }
    val snackbarVisible = remember { mutableStateOf(false) }

    LaunchedEffect(true){
        listJob = jobApi.getAllJob()
    }

    var settingExpand by remember { mutableStateOf(false) }
    
    var buttonRating by remember { mutableStateOf(0) }
    var buttonEndDate by remember { mutableStateOf(0) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(10.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "ПРОЕКТЫ КОМПАНИИ",
                        color = Color.White
                    )
                },
                backgroundColor = NavColor,
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else sheetState.collapse()
                        }
                        settingExpand = !settingExpand
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_tune_24),
                            contentDescription = "Сортировка",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
                    .padding(bottom = 55.dp)
            ){
                LazyColumn {
                    itemsIndexed(listJob) { _, item ->
//            ListTaskItem(userApi, jobApi, item, navController)
                        var expanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
//            .clickable {
//                navController.navigate("${Routes.SubTask.route}/${item.id}")
//            },
                            backgroundColor = NavColor,
                            elevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column() {
                                            Text(text = "Название", color = TextJobItem, fontSize = 12.sp)
                                            Text(
                                                text = "${item.name}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                        IconButton(modifier = Modifier
                                            .background(color = Red2)
                                            .size(30.dp), onClick = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                userApi.choiseTask(user.id, item.id)
                                                listJob = jobApi.getAllJob()
                                                snackbarVisible.value = true
                                            }
                                        }) {
                                            Icon(
                                                Icons.Filled.Add,
                                                contentDescription = "Добавить работу",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                }
                                AnimatedVisibility(visible = expanded) {
                                    Column() {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Log.d("Images ", "${item.images}")
                                            Carousel(images = item.images)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Column() {
                                                Text(text = "Адрес", color = TextJobItem, fontSize = 12.sp)
                                                Text(
                                                    text = "${item.address}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 15.sp
                                                )
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Column() {
                                                Text(
                                                    text = "Номер заказчика",
                                                    color = TextJobItem,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = "${item.phone}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 15.sp
                                                )
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Column() {
                                                Text(
                                                    text = "Описание",
                                                    color = TextJobItem,
                                                    fontSize = 12.sp
                                                )
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "${item.description}",
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 15.sp
                                                    )

                                                }

                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            item.rating?.let { RatingBar(rating = it) }
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Column() {
//                    Text(text = "Сроки выполнения", color = TextJobItem, fontSize = 12.sp)
//                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                        Text(text = "Начало: ", color = Color.White, fontSize = 15.sp)
//                        Text(text = "${item.beginDate}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
//                    }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Окончание: ",
                                                color = Color.White,
                                                fontSize = 15.sp
                                            )
                                            Text(
                                                text = "${item.endDate}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = NavColor),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 5.dp)){
                        Text(
                            text = "Сортировка",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                    Divider(color = Color.White, thickness = 1.dp)
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(2f)){
                        Row(
                            modifier = Modifier
                            .clickable(
                                onClick = {
                                    when (buttonRating) {
                                        0 -> {
                                            listJob =
                                                listJob.sortedBy { job: Job -> job.rating }
                                            buttonRating = 1
                                            rotate.value = (-180f).toInt()
                                        }
                                        1 -> {
                                            listJob =
                                                listJob.sortedByDescending { job: Job -> job.rating }
                                            buttonRating = 0
                                            rotate.value = 0
                                        }
                                    }
                                }
                            )
                        ) {
                            Text(
                                text = "По рейтингу",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Right
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(painter = painterResource(id = R.drawable.baseline_sort_24), contentDescription = "Вниз", tint = Color.White, modifier = Modifier
                                .size(27.dp)
                                .graphicsLayer {
                                    rotationX = rotate.value.toFloat()
                                })
                        }
                    }
                    Divider(color = Color.White, thickness = 1.dp)
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(2f)){
                        Row(modifier = Modifier
                            .clickable(
                                onClick = {
                                    when (buttonEndDate) {
                                        0 -> {
                                            listJob =
                                                listJob.sortedBy {
                                                    val endDate = it.endDate
                                                    dateFormat.parse(endDate!!)
                                                }
                                            buttonEndDate = 1
                                            label.value = R.drawable.baseline_trending_up_24
                                        }
                                        1 -> {
                                            listJob =
                                                listJob.sortedByDescending {
                                                    val endDate = it.endDate
                                                    dateFormat.parse(endDate!!)
                                                }
                                            buttonEndDate = 0
                                            label.value = R.drawable.baseline_trending_down_24
                                        }
                                    }
                                }
                            )) {
                            Text(
                                text = "По приоритету",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                                )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(painter = painterResource(id = label.value), contentDescription = "Вниз", tint = Color.White, modifier = Modifier.size(27.dp))
                        }

                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(2f)){

                    }
                }
            }
        }
    )
}