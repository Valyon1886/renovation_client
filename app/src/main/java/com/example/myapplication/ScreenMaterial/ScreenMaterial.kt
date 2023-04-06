package com.example.myapplication.ScreenMaterial

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.Material
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Models.MaterialInput
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.Retrofit.MaterialApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.ui.theme.TextJobItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenMaterial(
    jobId: Int, userApi: UserApi, materialApi: MaterialApi, navController: NavController
) {

    var listMaterial by remember { mutableStateOf<List<Material>>(emptyList()) }

    LaunchedEffect(true) {
        listMaterial = userApi.getUserMaterial(jobId)
    }

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    var label = remember { mutableStateOf(Icons.Filled.Add) }

    var keyboard = LocalSoftwareKeyboardController.current

    val material: Material

    var name = remember { mutableStateOf(TextFieldValue("")) }
    var cost = remember { mutableStateOf(TextFieldValue("")) }
    var count = remember { mutableStateOf(TextFieldValue("")) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(10.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(670.dp)
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
                        text = "Добавление материала"
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Название материала", color = Color.White) },
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
                        label = { Text("Стоимость", color = Color.White) },
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
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = count.value,
                        onValueChange = { count.value = it },
                        label = { Text("Количество", color = Color.White) },
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
                                val materialInput = MaterialInput(name.value.text, count.value.text.toIntOrNull(), cost.value.text.toIntOrNull())
                                materialApi.addMaterialToJob(materialInput, jobId)

                                listMaterial = userApi.getUserMaterial(jobId)

                                name.value = TextFieldValue("")
                                cost.value = TextFieldValue("")
                                count.value = TextFieldValue("")
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
                            text = "СОЗДАТЬ",
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "СПИСОК МАТЕРИАЛОВ",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = NavColor
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
            ) {
                LazyColumn {
                    itemsIndexed(listMaterial) { _, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
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
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column() {
                                            Text(
                                                text = "Название",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = item.name,
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                        Column() {
                                            Text(
                                                text = "Стоимость",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "${item.cost}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                        Column() {
                                            Text(
                                                text = "Количество",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "${item.count}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
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

                    name.value = TextFieldValue("")
                    cost.value = TextFieldValue("")
                    count.value = TextFieldValue("")

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