package com.example.myapplication.Composable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.Material
import com.example.myapplication.Models.JobInput
import com.example.myapplication.Models.MaterialInput
import com.example.myapplication.Navigation.Bottom_Navigation.Routes
import com.example.myapplication.R
import com.example.myapplication.Retrofit.JobApi
import com.example.myapplication.Retrofit.UserApi
import com.example.myapplication.ScreenJobs.ListJobItem
import com.example.myapplication.ScreenJobs.ListSubTask
import com.example.myapplication.ScreenJobs.ScreenSubTask
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun JobInfo(
    job: Job,
    listTask: List<Job>,
    navController: NavController,
    jobId: Int,
    jobApi: JobApi,
    userApi: UserApi
) {

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val materials = remember { mutableStateOf<List<Material>>(emptyList()) }
    var material by remember { mutableStateOf(Material("",0,0)) }

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    var label = remember { mutableStateOf(Icons.Filled.Add) }

    var keyboard = LocalSoftwareKeyboardController.current

    var endList = remember { mutableStateOf(listTask) }

    var listMaterial = remember { mutableStateOf<List<Material>>(emptyList()) }

//    var listMaterialInput = remember { mutableStateOf<List<Material>>(emptyList()) }

    var expanded by remember { mutableStateOf(false) }

    var expandedTagMaterial by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        endList.value = jobApi.getAllSubTask(jobId)
        listMaterial.value = userApi.getUserMaterial(jobId)
    }

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
                        text = "Создание задачи"
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Название проекта", color = Color.White) },
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
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Описание проекта", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

                    AnimatedVisibility(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                        visible = expandedTagMaterial)
                    {
                        Column() {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                text = "Добавленные материалы"
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                content = {
                                    items(materials.value){
                                        Card(modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(10.dp), backgroundColor = Red) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(3f)) {
                                                    Text(it.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                }
                                                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                                                    IconButton(onClick = {
                                                        materials.value -= it
                                                        Log.d("materials = ", "${materials.value}")
                                                    }) {
                                                        Icon(Icons.Default.Close, contentDescription = "Удалить материал из списка", tint = Color.White, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(Icons.Filled.List, contentDescription = "Показать список материалов", tint = Color.White)
                    }
                    AnimatedVisibility(visible = expanded) {
                        var expandedMenu by remember { mutableStateOf(false) }
                        var count = remember { mutableStateOf("") }
                        var nameMaterial by remember { mutableStateOf("Материал") }

                                Card(
                                    border = BorderStroke(1.dp,Color.Red),
                                    backgroundColor = NavColor,
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 7.dp)
                                ){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(modifier = Modifier.width(150.dp), contentAlignment = Alignment.CenterStart) {
                                            TextButton(onClick = { expandedMenu = true }) {
                                                Text(
                                                    text = nameMaterial,
                                                    fontSize = 18.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandedMenu,
                                                onDismissRequest = { expandedMenu = false }
                                            ) {
                                                Box(modifier = Modifier.fillMaxSize()){
                                                    Column() {
                                                        listMaterial.value.forEach() { item ->
                                                            DropdownMenuItem(onClick = {}){
                                                                Text(
                                                                    text = item.name,
                                                                    fontSize = 18.sp,
                                                                    modifier = Modifier
                                                                        .padding(10.dp)
                                                                        .clickable(onClick = {
                                                                            nameMaterial = item.name
                                                                            expandedMenu = false
                                                                            material.name = item.name
                                                                            material.cost = item.cost
                                                                        })
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.TopCenter){
                                            OutlinedTextField(
                                                value = count.value,
                                                onValueChange = {
                                                    count.value = it
                                                    material.count = count.value.toIntOrNull() ?: 0
//                                                    item.count = count.value.toIntOrNull() ?: 0
                                                },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                singleLine = false,
                                                shape = RoundedCornerShape(10.dp),
                                                textStyle = TextStyle(fontSize =  18.sp, fontWeight = FontWeight.Bold),
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    textColor = Color.White,
                                                    unfocusedBorderColor = BGColor,
                                                    focusedBorderColor = BGColor,
                                                    cursorColor = BGColor
                                                )
                                            )
                                        }
                                        Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.CenterEnd){
                                            IconButton(
                                                onClick = {
                                                    materials.value += material.copy()
                                                    Log.d("materials = ", "${materials.value}")
                                                    expandedTagMaterial = true
                                                }
                                            ) {
                                                Icon(Icons.Filled.Add, contentDescription = "Добавить материал", tint = Color.White)
                                            }
                                        }
                                    }
                                }
                            }

//                    AnimatedVisibility(visible = expanded) {
//                        LazyColumn(
//                            modifier = Modifier.fillMaxWidth()
//                        ){
//                            itemsIndexed(listMaterial.value){_, item ->
//                                var isChecked = remember { mutableStateOf(false) }
//                                var count = remember { mutableStateOf("") }
//                                Card(
//                                    border = BorderStroke(1.dp,Color.Red),
//                                    backgroundColor = NavColor,
//                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 7.dp)
//                                ){
//                                    Row(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(3.dp),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.CenterStart){
//                                            Text(
//                                                item.name,
//                                                fontSize = 15.sp,
//                                                color = Color.White,
//                                                modifier = Modifier.padding(start = 5.dp)
//                                            )
//                                        }
//                                        Box(modifier = Modifier.width(150.dp), contentAlignment = Alignment.TopCenter){
//                                            OutlinedTextField(
//                                                value = count.value,
//                                                onValueChange = {
//                                                    count.value = it
//                                                    item.count = count.value.toIntOrNull() ?: 0
//                                                },
//                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                                                singleLine = false,
//                                                textStyle = TextStyle(fontSize =  15.sp),
//                                                colors = TextFieldDefaults.outlinedTextFieldColors(
//                                                    textColor = Color.White,
//                                                    unfocusedBorderColor = BGColor,
//                                                    focusedBorderColor = BGColor,
//                                                    cursorColor = BGColor
//                                                )
//                                            )
//                                        }
//                                        Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.CenterEnd){
//                                            Checkbox(
//                                                checked = isChecked.value,
//                                                onCheckedChange = {
//                                                    isChecked.value = it
//                                                    if(it)
//                                                        materials.value += item
//                                                    else materials.value -= item
//                                                    Log.d("materials = ", "${materials.value}")
//                                                },
//                                                colors = CheckboxDefaults.colors(
//                                                    checkedColor = Red,
//                                                    uncheckedColor = Color.DarkGray,
//                                                    checkmarkColor = Color.White
//                                                )
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val jobInput = JobInput(name.value.text, description.value.text, materials.value.toMutableList())
                                Log.d("JobInput", "${jobInput.materials}")
                                jobApi.addTaskToJob(jobInput, jobId)

                                endList.value = jobApi.getAllSubTask(jobId)

                                name.value = TextFieldValue("")
                                description.value = TextFieldValue("")
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
                        text = "ПЛАН",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
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
                actions = {
                    IconButton(onClick = { navController.navigate("${Routes.Material.route}/${job.id}") }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Материалы",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = NavColor
            )
        },
        content = {
            job.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BGColor),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        backgroundColor = NavColor
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                text = "${it.name}"
                            )
                            Text(text = "Описание", color = TextJobItem, fontSize = 12.sp)
                            Text(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color.White,
                                text = "${it.description}"
                            )
                        }
                    }
                    LazyColumn {
                        itemsIndexed(endList.value) { _, item ->
//                            ListSubTask(item, userApi, navController, it.id)
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
                                                    text = "${item.name}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 15.sp
                                                )
                                            }
                                            IconButton(modifier = Modifier.size(30.dp), onClick = {
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    userApi.deleteTask(item.id, jobId)
                                                    endList.value = jobApi.getAllSubTask(jobId)
                                                }
                                            }) {
                                                Icon(
                                                    Icons.Filled.Delete,
                                                    contentDescription = "Добавить работу",
                                                    tint = Color.White
                                                )
                                            }
                                        }

                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                    ) {
                                        Column() {
                                            Text(
                                                text = "Описание",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "${item.description}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                    ) {
                                        Column() {
                                            Text(
                                                text = "Сроки выполнения",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Начало: ",
                                                    color = Color.White,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    text = "${item.beginDate}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 15.sp
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Окончание: ",
                                                    color = Color.White,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    text = "${item.endDate}",
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
                    description.value = TextFieldValue("")

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
