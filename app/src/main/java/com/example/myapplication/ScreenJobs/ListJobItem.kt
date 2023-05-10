package com.example.myapplication.ScreenJobs

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.TextJobItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.applyConnectionSpec

@Composable
fun ListJobItem(item: Job, navController: NavController, userApi: UserApi, user: User, mainActivity: MainActivity){

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
                Column() {
                    Text(text = "Название", color = TextJobItem, fontSize = 12.sp)
                    Text(text = "${item.name}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
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
                    .padding(5.dp)
            ){
                Button(onClick = {
                    if (item.subTasks?.isEmpty() == true){
                        CoroutineScope(Dispatchers.IO).launch {
                            userApi.finishTask(user.id, item.id)
                            mainActivity.runOnUiThread{
                                Toast.makeText(mainActivity, "Работа выполнена! Отчет в процессе генерации.", Toast.LENGTH_SHORT).show()
                            }
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

                }
            }
        }

    }
}