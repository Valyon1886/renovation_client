package com.example.myapplication.ScreenTasks

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import coil.compose.rememberImagePainter
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ListJobItem
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red2
import com.example.myapplication.ui.theme.TextJobItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Orange
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.lang.Math.ceil
import java.lang.Math.floor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(userApi: UserApi, jobApi: JobApi, navController: NavController, user: User) {
    var job by remember { mutableStateOf<Job?>(null) }
    var listJob by remember { mutableStateOf<List<Job>>(emptyList()) }
    val snackbarVisible = remember { mutableStateOf(false) }

    LaunchedEffect(true){
        listJob = jobApi.getAllJob()
    }

    var buttonRating by remember { mutableStateOf(0) }


    Column(){
        Button(onClick = {
            when(buttonRating){
                0 -> {
                    listJob = listJob.sortedBy { job: Job -> job.rating }
                    buttonRating = 1
                }
                1 -> {
                    listJob = listJob.sortedByDescending { job: Job -> job.rating }
                    buttonRating = 0
                }
            }

            Log.d("Список работ отсортирован = ", "$listJob")
        }) {
            Text(text = "По рейтингу")
        }
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
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Carousel(images: MutableList<String>?) {
    val pageCount = 3
    val pagerState = rememberPagerState()

    Column{
        HorizontalPager(
            count = pageCount,
            state = pagerState,
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier
        ) { page ->
            Column() {
                Image(
                    painter = rememberAsyncImagePainter(model = images?.get(page)),
                    contentDescription = "Карусель",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(325.dp)
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        }
        Row(
            Modifier
                .height(25.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Orange,
) {
    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_half_24),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_outline_24),
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}