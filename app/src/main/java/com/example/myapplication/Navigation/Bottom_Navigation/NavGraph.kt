package com.example.myapplication.Navigation.Bottom_Navigation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.ScreenDocx.ScreenDocx
import com.example.myapplication.Retrofit.EmployerApi
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.MaterialApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ScreenCreateJob
import com.example.myapplication.ScreenJobs.ScreenJobs
import com.example.myapplication.ScreenJobs.ScreenSubTask
import com.example.myapplication.ScreenMaterial.ScreenMaterial
import com.example.myapplication.ScreenProfile.ScreenProfile
import com.example.myapplication.ScreenTasks.ScreenTasks
import com.example.myapplication.ScreenUserRegistration.ScreenUserRegistration
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    jobApi: JobApi,
    materialApi: MaterialApi,
    userApi: UserApi,
    employerApi: EmployerApi,
    auth: FirebaseAuth,
    mainActivity: MainActivity
) {

    var user by remember { mutableStateOf<User?>(null) }
//
//    CoroutineScope(Dispatchers.IO).launch {
//        user = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
//    }

    var checkIdToken by remember { mutableStateOf(false) }
    val isLoading = remember { AtomicBoolean(true) }

    LaunchedEffect(Unit) {
        checkIdToken = userApi.checkIdTokenUser(auth.currentUser?.uid.toString()) == true
        if (checkIdToken) user = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
        isLoading.set(false)
    }

    NavHost(
        navController = navHostController,
        startDestination = if (user != null) "tasks" else "user_registration"
    ) {
        composable("profile") {
            ScreenProfile(userApi, auth, mainActivity, user!!)
        }
        composable("jobs") {
            ScreenJobs(userApi, jobApi, navHostController, auth, user!!)
        }
        composable("tasks") {
            ScreenTasks(userApi, jobApi, navHostController, user!!)
        }
        composable("docx") {
            ScreenDocx()
        }
        composable("${Routes.Material.route}/{id}") { navBackStack ->
            val jobId = navBackStack.arguments?.getString("id")?.toInt() ?: 0
            ScreenMaterial(jobId = jobId, userApi, materialApi, navHostController)
        }
        composable(Routes.CreateJob.route) {
            ScreenCreateJob(jobApi, navHostController)
        }
        composable("${Routes.SubTask.route}/{id}") { navBackStack ->
            val jobId = navBackStack.arguments?.getString("id")?.toInt() ?: 0
            ScreenSubTask(jobId = jobId, jobApi, navHostController, userApi, user!!)
        }
        composable("user_registration") {
            ScreenUserRegistration(userApi, navHostController, auth, mainActivity)
        }
    }

}