package com.example.myapplication.Composable

import android.opengl.Visibility
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.Retrofit.DocumentApi
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ListJobItem
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.ui.theme.TextJobItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JobItem(userApi: UserApi, jobApi: JobApi, documentApi: DocumentApi, navController: NavController, auth: FirebaseAuth, user: User, mainActivity: MainActivity) {
    var job by remember { mutableStateOf<Job?>(null) }
    var listJob by remember { mutableStateOf<List<Job>>(emptyList()) }
    var userUpdate by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(true){
        listJob = userApi.getUserTask(user.id)
    }


    LazyColumn{
        itemsIndexed(listJob){_, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable {
                        navController.navigate("${Routes.SubTask.route}/${item.id}")
                    },
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
                    ){
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            var visibleState by remember { mutableStateOf(false) }
                            if(item.subTasks?.isEmpty() == false) visibleState = true
                            Log.d("${item.name} ", "${visibleState}")
                            Box(contentAlignment = Alignment.CenterStart){
                                Column() {
                                    Text(text = "Название", color = TextJobItem, fontSize = 12.sp)
                                    Text(
                                        text = "${item.name}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            AnimatedVisibility(visible = visibleState){
                                Box(){
                                    Text(
                                        text = "${if (item.completedSubTasks?.count() == null) 0 else item.completedSubTasks?.count()}/" +
                                                "${if(item.completedSubTasks?.count() == null) item.subTasks?.count() else item.subTasks?.count()?.plus(item.completedSubTasks?.count()!!)}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        textAlign = TextAlign.Right
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ){
                        Column() {
                            Text(text = "Адрес", color = TextJobItem, fontSize = 12.sp)
                            Text(text = "${item.address}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ){
                        Column() {
                            Text(text = "Номер заказчика", color = TextJobItem, fontSize = 12.sp)
                            Text(text = "${item.phone}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ){
                        Column() {
                            Text(text = "Описание", color = TextJobItem, fontSize = 12.sp)
                            Text(text = "${item.description}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ){
                        Column() {
                            Text(text = "Сроки выполнения", color = TextJobItem, fontSize = 12.sp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Начало: ", color = Color.White, fontSize = 15.sp)
                                Text(text = "${item.beginDate}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Окончание: ", color = Color.White, fontSize = 15.sp)
                                Text(text = "${item.endDate}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(3.dp, Color.Black),
                            contentPadding = PaddingValues(8.dp),
                            shape = RoundedCornerShape(15.dp),
                            onClick = {
                            if (item.subTasks?.isEmpty() == true){
                                CoroutineScope(Dispatchers.IO).launch {
                                    userApi.finishTask(user.id, item.id)
                                    mainActivity.runOnUiThread{
                                        Toast.makeText(mainActivity, "Работа выполнена! Отчет в процессе генерации.", Toast.LENGTH_SHORT).show()
                                    }
                                    listJob = userApi.getUserTask(user.id)
                                    delay(2000)
                                    userUpdate = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
                                    Log.d("Task ", userUpdate.toString())
                                    userUpdate?.completedTasks?.size?.minus(1)
                                        ?.let { documentApi.generateDoc(userUpdate!!, it) }

                                }
                            }
                            else
                                Toast.makeText(mainActivity, "Вы еще не все задачи закрыли!", Toast.LENGTH_SHORT).show()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        if (item.subTasks?.isEmpty() == true){
//                            userApi.finishTask(user.id, item.id)
//                            Toast.makeText(mainActivity, "Работа выполнена! Отчет в процессе генерации.", Toast.LENGTH_SHORT).show()
//                        }
//                        else{
//                            Toast.makeText(mainActivity, "Вы еще не все задачи закрыли!", Toast.LENGTH_SHORT).show()
//                        }
//                    }
                        }) {
                            Text(
                                text = "Завершить проект",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }

            }
        }
    }
}
