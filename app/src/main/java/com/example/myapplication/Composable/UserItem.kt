package com.example.myapplication.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.Entity.User
import com.example.myapplication.Retrofit.UserApi

//@Composable
//fun UserItem(userApi: UserApi){
//    var user by remember { mutableStateOf<User?>(null) }
//
//    LaunchedEffect(true) {
//        user = userApi.getUser(102)
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize().background(color = Color.Yellow),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        if (user != null) {
//            Text(text = "User Name: ${user!!.userName}")
//            Text(text = "User Email: ${user!!.password}")
//        } else {
//            Text(text = "Loading user...")
//        }
//    }
//}