package com.example.myapplication.Navigation.Bottom_Navigation


sealed class Routes(val route: String) {
    object CreateJob: Routes("create_job")
    object SubTask: Routes("subTask")
    object Material: Routes("material")
    object UserRegistration: Routes("user_registration")
}