package com.example.myapplication.Auth

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.AuthActivity
import com.example.myapplication.Navigation.Bottom_Navigation.BottomNavigation
import com.example.myapplication.Navigation.Bottom_Navigation.NavGraph

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthMainScreen(authActivity: AuthActivity){
    val navController = rememberNavController()

    Scaffold() {
        NavGraphAuth(navHostController = navController, authActivity)
    }
}