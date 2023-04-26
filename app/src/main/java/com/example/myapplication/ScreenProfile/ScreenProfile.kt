package com.example.myapplication.ScreenProfile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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
import com.example.myapplication.Entity.Employer
import com.example.myapplication.Entity.EmployerOfUser
import com.example.myapplication.Entity.MainMaterial
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.EmployerInput
import com.example.myapplication.Models.MaterialInput
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.R
import com.example.myapplication.Retrofit.UserApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "ShowToast")
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

    var listEmployer by remember { mutableStateOf<List<EmployerOfUser>>(emptyList()) }

    LaunchedEffect(true) {
        listEmployer = userApi.getUserEmployerOfUser(user.id)
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
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
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
                                        text = "30",
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
                        backgroundColor = NavColor
                    ) {
                        Column() {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(4f),
                                contentAlignment = Alignment.TopCenter
                            ){
                                Column() {
                                    LazyColumn {
                                        itemsIndexed(listEmployer) { _, item ->
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                backgroundColor = Red,
                                                shape = RoundedCornerShape(10.dp),
                                                elevation = 10.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ){
                                                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(2f)) {
                                                        Column(){
                                                            Box(
                                                                contentAlignment = Alignment.CenterStart
                                                            ) {
                                                                Text(
                                                                    text = item.post
                                                                )
                                                            }
                                                            Box(
                                                                contentAlignment = Alignment.CenterStart
                                                            ) {
                                                                Text(
                                                                    text = "${item.lastName} ${item.firstName} ${item.secondName}"
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
                                                                Icons.Filled.Delete,
                                                                contentDescription = "Удалить напарника",
                                                                tint = Color.White
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
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