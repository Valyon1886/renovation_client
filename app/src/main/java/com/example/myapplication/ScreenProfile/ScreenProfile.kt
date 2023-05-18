package com.example.myapplication.ScreenProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.myapplication.Entity.*
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.EmployerInput
import com.example.myapplication.Models.MaterialInput
import com.example.myapplication.Models.UserInput
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.R
import com.example.myapplication.Retrofit.UserApi
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "ShowToast", "RememberReturnType")
@Composable
fun ScreenProfile(userApi: UserApi, auth: FirebaseAuth, mainActivity: MainActivity, user: User) {
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()
    var keyboard = LocalSoftwareKeyboardController.current
    var label = remember { mutableStateOf(Icons.Filled.Add) }

    var firstName = remember { mutableStateOf(TextFieldValue("")) }
    var secondName = remember { mutableStateOf(TextFieldValue("")) }
    var lastName = remember { mutableStateOf(TextFieldValue("")) }
    var post = remember { mutableStateOf(TextFieldValue("")) }
    var cost = remember { mutableStateOf(TextFieldValue("")) }

    var userImgSrc by remember { mutableStateOf<User?>(null) }

    var listEmployer by remember { mutableStateOf<List<EmployerOfUser>>(emptyList()) }

    val imageUriState = remember { mutableStateOf<Uri?>(null) }
    var uploadUri: Uri? = null
    val storage = Firebase.storage
    var storageRef = storage.reference
    var imDB: ImageView? = null

    Log.d("uploadUri", user.imgSrc)

//    fun downloadImage(){
//        Picasso.get().load(user.imgSrc).into(imDB)
//        imageBitmapState.value = imDB?.drawable?.toBitmap()?.asImageBitmap()
//        Log.d("Image ", "${imageBitmapState.value}")
//    }

    fun saveUser(uploadUri: Uri?){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("imgSrc = ", " ${uploadUri.toString()}")
            val userImg = UserImg(uploadUri.toString())
            userApi.updateUserImg(user.id, userImg)
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
            uploadImage()
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
    } ?: rememberAsyncImagePainter(model = userImgSrc?.imgSrc)

    LaunchedEffect(true) {
        listEmployer = userApi.getUserEmployerOfUser(user.id)
        userImgSrc = userApi.getUserByIdToken(auth.currentUser?.uid.toString())
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(10.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ПРОФИЛЬ",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {

                },
                actions = {
                    IconButton(onClick = {
                        mainActivity.signOut()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = "Выход из аккаунта",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = NavColor
            )
        },
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(560.dp)
                    .background(color = NavColor),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        text = "Добавление напарника"
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = firstName.value,
                        onValueChange = { firstName.value = it },
                        label = { Text("Имя", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            unfocusedBorderColor = BGColor,
                            focusedBorderColor = BGColor,
                            cursorColor = BGColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = secondName.value,
                        onValueChange = { secondName.value = it },
                        label = { Text("Отчество", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            unfocusedBorderColor = BGColor,
                            focusedBorderColor = BGColor,
                            cursorColor = BGColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = lastName.value,
                        onValueChange = { lastName.value = it },
                        label = { Text("Фамилия", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            unfocusedBorderColor = BGColor,
                            focusedBorderColor = BGColor,
                            cursorColor = BGColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = post.value,
                        onValueChange = { post.value = it },
                        label = { Text("Должность", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            unfocusedBorderColor = BGColor,
                            focusedBorderColor = BGColor,
                            cursorColor = BGColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = cost.value,
                        onValueChange = { cost.value = it },
                        label = { Text("Зарплата в час", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = false,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            unfocusedBorderColor = BGColor,
                            focusedBorderColor = BGColor,
                            cursorColor = BGColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val employerInput = EmployerInput(firstName.value.text, secondName.value.text, lastName.value.text, post.value.text, cost.value.text.toInt())
                                userApi.addEmployerToUser(user.id, employerInput)

                                listEmployer = userApi.getUserEmployerOfUser(user.id)
                                mainActivity.runOnUiThread{
                                    Toast.makeText(mainActivity, "Напарник добавлен в Вашу команду", Toast.LENGTH_SHORT).show()
                                }
                                firstName.value = TextFieldValue("")
                                secondName.value = TextFieldValue("")
                                lastName.value = TextFieldValue("")
                                post.value = TextFieldValue("")
                                cost.value = TextFieldValue("")


                            }

                            scope.launch {
                                sheetState.collapse()
                            }
                            label.value =
                                if (label.value == Icons.Filled.Add) Icons.Filled.ArrowDropDown else Icons.Filled.Add
                            keyboard?.hide()
                        },
                        colors = ButtonDefaults.buttonColors(Red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Добавить напарника",
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f),
                    contentAlignment = Alignment.Center
                ){

                    Box(){
                        Image(
                            painter = painter,
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape)
                                .border(2.dp, color = Color.White, CircleShape)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "pencil",
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp)
                                .aspectRatio(1f)
                                .padding(end = 5.dp, bottom = 5.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .clickable {
                                    getImage()
                                }
                                .border(2.dp, color = Color.White, CircleShape)
                                .scale(0.75f)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    Card(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f),
                                contentAlignment = Alignment.Center
                            ){
                                Column() {
                                    Text(
                                        text = "${if (user.completedTasks?.count() == null) 0 else user.completedTasks?.count()}",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "Заказы",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f),
                                contentAlignment = Alignment.Center
                            ){
                                Column() {
                                    Text(
                                        text = "2,467",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "КПД",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f),
                                contentAlignment = Alignment.Center
                            ){
                                Column() {
                                    Text(
                                        text = "4.8",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "Рейтинг",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Бригада",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    contentAlignment = Alignment.TopCenter
                ){
                    Card(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        backgroundColor = NavColor,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column() {
                            LazyColumn {
                                itemsIndexed(listEmployer) { _, item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(3f)) {
                                                Column(modifier = Modifier.padding(5.dp)){
                                                    Box(
                                                        contentAlignment = Alignment.CenterStart
                                                    ) {
                                                        Text(
                                                            text = item.post,
                                                            color = Color.White,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    }
                                                    Box(
                                                        contentAlignment = Alignment.CenterStart
                                                    ) {
                                                        Text(
                                                            text = "${item.lastName} ${item.firstName} ${item.secondName}",
                                                            color = Color.White,
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.weight(1f)){
                                                IconButton(modifier = Modifier.size(30.dp), onClick = {
                                                    CoroutineScope(Dispatchers.IO).launch {
                                                        userApi.deleteEmployerFromUser(user.id, item.id)
                                                        listEmployer = userApi.getUserEmployerOfUser(user.id)
                                                    }
                                                }) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.baseline_person_off_24),
                                                        contentDescription = "Удалить напарника",
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Divider(color = Color.White, thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Color.Red)
                ){

                }
            }
        },
        floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 55.dp),
                backgroundColor = Red,
                onClick = {
                    scope.launch {
                        if (sheetState.isCollapsed) {
                            sheetState.expand()
                        } else sheetState.collapse()
                    }

                    firstName.value = TextFieldValue("")
                    secondName.value = TextFieldValue("")
                    lastName.value = TextFieldValue("")
                    post.value = TextFieldValue("")
                    cost.value = TextFieldValue("")

                    keyboard?.hide()
                    label.value =
                        if (label.value == Icons.Filled.Add) Icons.Filled.ArrowDropDown else Icons.Filled.Add
                },
            ) {
                Icon(imageVector = label.value, contentDescription = "fab icon", tint = Color.White)
            }
        }
    )
}