package com.example.myapplication.ScreenJobs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.ui.theme.Shapes
import com.example.myapplication.ui.theme.TextJobItem

@Composable
fun ListSubTask(item: Job, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate("${Routes.SubTask.route}/${item.id}")
            },
        backgroundColor = NavColor,
        shape = RoundedCornerShape(10.dp),
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
        }
    }
}