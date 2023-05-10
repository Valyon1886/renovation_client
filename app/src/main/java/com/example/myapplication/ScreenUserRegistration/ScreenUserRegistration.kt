package com.example.myapplication.ScreenUserRegistration

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHost
import androidx.viewpager2.widget.ViewPager2
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Auth.RoutesAuth
import com.example.myapplication.Entity.Material
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Models.UserInput
import com.example.myapplication.Navigation.Bottom_Navigation.BottomItem
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.R
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenTasks.TaskItem
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenUserRegistration(userApi: UserApi, navController: NavController, auth: FirebaseAuth, mainActivity: MainActivity) {
    var firstNameState by remember { mutableStateOf(TextFieldValue()) }
    var secondNameState by remember { mutableStateOf(TextFieldValue()) }
    var lastNameState by remember { mutableStateOf(TextFieldValue()) }

    val imageUriState = remember { mutableStateOf<Uri?>(null) }
    val storage = Firebase.storage
    var storageRef = storage.reference
    var uploadUri: Uri? = null
    var imgSrc: String = ""

    fun saveUser(uploadUri: Uri?){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("UserInput = ", "${firstNameState.text}, ${secondNameState.text}, ${lastNameState.text}, ${auth.currentUser?.uid.toString()}, ${uploadUri.toString()}")
            val userInput = UserInput(firstNameState.text, secondNameState.text, lastNameState.text, auth.currentUser?.uid.toString(), uploadUri.toString())
            userApi.addUser(userInput)

            withContext(Dispatchers.Main) {
                navController.navigate("tasks")
            }
        }
    }

    fun uploadImage() {
        storageRef = Firebase.storage.reference.child("images/${auth.currentUser?.uid}/${imageUriState.value?.lastPathSegment}")
        val uploadTask = imageUriState.value?.let { storageRef.putFile(it) }

        uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                uploadUri = task.result
                Log.d("uploadUri", "${uploadUri}")
                saveUser(uploadUri)
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUriState.value = uri
            }
            Log.d("Image", "Uri ${imageUriState.value}")
//            uploadImage{ uploadUri ->
//                imgSrc = uploadUri?.toString() ?: ""
//                Log.d("imgSrc ", imgSrc)
//                onImageUploaded(imgSrc)
//            }
        }
    }

    fun getImage(){
        val intentChooser = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        launcher.launch(intentChooser)
    }

    val painter = imageUriState.value?.let { uri ->
        rememberAsyncImagePainter(model = uri)
    } ?: painterResource(id = R.drawable.image)

//    var user by remember { mutableStateOf(UserInput("","","", "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Анкета бригадира",
                        color = Color.White
                    )
                },
                backgroundColor = NavColor
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Column() {
                        Image(
                            painter = painter,
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter,
                            modifier = Modifier
                                .size(200.dp)
                                .border(
                                    BorderStroke(5.dp, Color.Yellow),
                                    CircleShape
                                )
                                .padding(5.dp)
                                .clip(CircleShape)
                                .clickable{
                                    getImage()
                                }
                        )
                        Button(onClick = {
                            getImage()
                        }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = "Изменить картинку профиля")
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(){
                        OutlinedTextField(
                            value = firstNameState,
                            onValueChange = { firstNameState = it },
                            label = { Text("Имя") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Email Icon"
                                )
                            },
                            shape = RoundedCornerShape(25.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = secondNameState,
                            onValueChange = { secondNameState = it },
                            label = { Text("Отчество") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "SecondName Icon"
                                )
                            },
                            shape = RoundedCornerShape(25.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = lastNameState,
                            onValueChange = { lastNameState = it },
                            label = { Text("Фамилия") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "LastName Icon"
                                )
                            },
                            shape = RoundedCornerShape(25.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                uploadImage()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(Color.Blue)
                        ) {
                            Text(
                                text = "Отправить",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}
