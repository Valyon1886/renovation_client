package com.example.myapplication.ScreenTasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red2
import com.example.myapplication.ui.theme.TextJobItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListTaskItem(userApi: UserApi, jobApi: JobApi, item: Job, navController: NavController){
    var expanded by remember { mutableStateOf(false) }

//    val imageBytes = Base64.getDecoder().decode(item.image)
//    val outputStream = ByteArrayOutputStream()
//    outputStream.write(imageBytes)
//    val image = ImageIO.read(ByteArrayInputStream(outputStream.toByteArray()))

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
            ){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column() {
                        Text(text = "Название", color = TextJobItem, fontSize = 12.sp)
                        Text(text = "${item.name}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                    }
                    IconButton(modifier = Modifier.background(color = Red2).size(30.dp), onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            userApi.chosieTask(1, item.id)
                        }
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Добавить работу", tint = Color.White)
                    }
                }

            }
            AnimatedVisibility(visible = expanded) {
                Column() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ){
//                        Image(painter = painterResource(id = Base64.getDecoder().decode(item.image)), contentDescription = "image")
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
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "${item.description}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)

                            }

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
//                    Text(text = "Сроки выполнения", color = TextJobItem, fontSize = 12.sp)
//                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                        Text(text = "Начало: ", color = Color.White, fontSize = 15.sp)
//                        Text(text = "${item.beginDate}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
//                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Окончание: ", color = Color.White, fontSize = 15.sp)
                        Text(text = "${item.endDate}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}