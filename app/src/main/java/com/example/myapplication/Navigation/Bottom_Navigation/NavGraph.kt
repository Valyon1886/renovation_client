package com.example.myapplication.Navigation.Bottom_Navigation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.Retrofit.*
import com.example.myapplication.ScreenDocx.ScreenDocx
import com.example.myapplication.ScreenJobs.ScreenCreateJob
import com.example.myapplication.ScreenJobs.ScreenJobs
import com.example.myapplication.ScreenJobs.ScreenSubTask
import com.example.myapplication.ScreenMaterial.ScreenMaterial
import com.example.myapplication.ScreenProfile.ScreenProfile
import com.example.myapplication.ScreenTasks.ScreenTasks
import com.example.myapplication.ScreenUserRegistration.ScreenUserRegistration
import com.example.myapplication.ui.theme.Red2
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    documentApi: DocumentApi,
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

//    LaunchedEffect(Unit) {
//        checkIdToken = userApi.checkIdTokenUser(auth.currentUser?.uid.toString()) == true
//        if (checkIdToken) user = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
//        isLoading.set(false)
//    }

    NavHost(
        navController = navHostController,
        startDestination = "loading"
    ) {
        composable("profile") {
            ScreenProfile(userApi, auth, mainActivity, user!!)
        }
        composable("jobs") {
            ScreenJobs(userApi, jobApi, documentApi, navHostController, auth, user!!, mainActivity)
        }
        composable("tasks") {
            ScreenTasks(userApi, jobApi, navHostController, user!!)
        }
        composable("docx") {
            ScreenDocx(documentApi, mainActivity, user!!)
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
        composable("loading") {
            ScreenLoading()
        }
    }

    LaunchedEffect(Unit) {
        delay(5000)
        isLoading.set(true)
        checkIdToken = userApi.checkIdTokenUser(auth.currentUser?.uid.toString()) == true
        if (checkIdToken) user = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
        isLoading.set(false)
        if (user == null) {
            navHostController.navigate("user_registration")
        } else {
            navHostController.navigate("tasks")
        }
    }

}

@Preview
@Composable
fun ScreenLoading() {
    val progressValue = 1f
    val infiniteTransition = rememberInfiniteTransition()

    val progressAnimationValue by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = progressValue,animationSpec = infiniteRepeatable(animation = tween(3000)))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                strokeWidth = 7.dp,
                color = Red2,
                progress = progressAnimationValue
            )
            Spacer(modifier = Modifier.width(width = 20.dp))
            Text(
                text = "Загружаем задачи...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}