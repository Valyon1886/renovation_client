package com.example.myapplication.Composable

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.CaseMap.Title
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Entity.Employer
import com.example.myapplication.Entity.Job
import com.example.myapplication.Entity.Material
import com.example.myapplication.Entity.User
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
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun JobInfo(
    job: Job,
    listTask: List<Job>,
    navController: NavController,
    jobId: Int,
    jobApi: JobApi,
    userApi: UserApi,
    user: User
) {

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val materials = remember { mutableStateOf<List<Material>>(emptyList()) }
    var material by remember { mutableStateOf(Material("",0,0)) }

    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    // Declaring a string value to
    // store date in string format
    val beginDate = "$mDay/${mMonth+1}/$mYear"
    val endDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            endDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    var label = remember { mutableStateOf(Icons.Filled.Add) }

    var keyboard = LocalSoftwareKeyboardController.current

    var endList = remember { mutableStateOf(listTask) }

    var listMaterial = remember { mutableStateOf<List<Material>>(emptyList()) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

//    var listMaterialInput = remember { mutableStateOf<List<Material>>(emptyList()) }

    var expanded by remember { mutableStateOf(false) }
    var expandedTagMaterial by remember { mutableStateOf(false) }

    var expandedEmployer by remember { mutableStateOf(false) }
    var expandedTagEmployer by remember { mutableStateOf(false) }
    var listEmployer = remember { mutableStateOf<List<Employer>>(emptyList()) }
    val employers = remember { mutableStateOf<List<Employer>>(emptyList()) }
    var employer by remember { mutableStateOf(Employer("","","", "", 0, 0)) }

    LaunchedEffect(true) {
        endList.value = jobApi.getAllSubTask(jobId)
        listMaterial.value = userApi.getUserMaterial(jobId)
        listEmployer.value = userApi.getUserEmployer(user.id)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
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
        sheetShape = RoundedCornerShape(10.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(800.dp)
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
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
                    Box(
                        modifier = Modifier.background(color = Red)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                            // Creating a button that on
                            // click displays/shows the DatePickerDialog
                            IconButton(
                                onClick = {
                                    mDatePickerDialog.show()
                                }
                            ) {
                                Icon(painterResource(id = R.drawable.baseline_calendar_month_24), contentDescription = "Календарь", tint = Color.White)
                            }
                            // Displaying the mDate value in the Text
                            Text(text = "Конечная дата: ${endDate.value}", fontSize = 20.sp, textAlign = TextAlign.Center)
                        }
                    }
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
                                                        if (materials.value.isEmpty()) expandedTagMaterial = false
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
                    AnimatedVisibility(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                        visible = expandedTagEmployer)
                    {
                        Column() {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                text = "Добавленные напарники"
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                content = {
                                    items(employers.value){
                                        Card(modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(10.dp), backgroundColor = Red) {
                                            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(3f)) {
                                                    Text(it.post, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                }
                                                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                                                    IconButton(onClick = {
                                                        employers.value -= it
                                                        Log.d("employers = ", "${employers.value}")
                                                        if (employers.value.isEmpty()) expandedTagMaterial = false
                                                    }) {
                                                        Icon(Icons.Default.Close, contentDescription = "Удалить напарника из списка", tint = Color.White, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                            IconButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(
                                    Icons.Filled.List,
                                    contentDescription = "Показать список материалов",
                                    tint = Color.White
                                )
                            }
                        }
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                            IconButton(
                                onClick = { expandedEmployer = !expandedEmployer }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_person_add_alt_1_24),
                                    contentDescription = "Показать список напарников",
                                    tint = Color.White
                                )
                            }
                        }

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
                                                                            material.name =
                                                                                item.name
                                                                            material.cost =
                                                                                item.cost
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
                    AnimatedVisibility(visible = expandedEmployer) {
                        var expandedMenuEmployer by remember { mutableStateOf(false) }
                        var postEmployer by remember { mutableStateOf("Должность") }
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
                                    TextButton(onClick = { expandedMenuEmployer = true }) {
                                        Text(
                                            text = postEmployer,
                                            fontSize = 18.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expandedMenuEmployer,
                                        onDismissRequest = { expandedMenuEmployer = false }
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()){
                                            Column() {
                                                listEmployer.value.forEach() { item ->
                                                    DropdownMenuItem(onClick = {}){
                                                        Text(
                                                            text = item.post,
                                                            fontSize = 18.sp,
                                                            modifier = Modifier
                                                                .padding(10.dp)
                                                                .clickable(onClick = {
                                                                    postEmployer = item.post
                                                                    expandedMenuEmployer = false
                                                                    employer.firstName =
                                                                        item.firstName
                                                                    employer.secondName =
                                                                        item.secondName
                                                                    employer.lastName =
                                                                        item.lastName
                                                                    employer.post = item.post
                                                                    employer.cost = item.cost
                                                                    employer.clock = item.clock
                                                                })
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.CenterEnd){
                                    IconButton(
                                        onClick = {
                                            employers.value += employer.copy()
                                            Log.d("employers = ", "${employers.value}")
                                            expandedTagEmployer = true
                                        }
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "Добавить напарника", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val jobInput = JobInput(
                                    name.value.text,
                                    description.value.text,
                                    materials.value.toMutableList(),
                                    employers.value.toMutableList(),
                                    beginDate, endDate.value)
                                Log.d("JobInput", "${jobInput.materials} ${jobInput.employers}")
                                jobApi.addTaskToJob(jobInput, jobId)

                                endList.value = jobApi.getAllSubTask(jobId)

                                //очищаются поля с названием и описанием
                                name.value = TextFieldValue("")
                                description.value = TextFieldValue("")
                                //закрывается поле с выбранными материалами
                                expandedTagMaterial = false
                                //очищаем список с материалами
                                materials.value = emptyList()
                                //скрываем поле добавления материалов
                                expanded = false
                                //закрывается поле с выбранными напарниками
                                expandedTagEmployer = false
                                //очищаем список с напарниками
                                employers.value = emptyList()
                                //скрываем поле добавления напарников
                                expandedEmployer = false
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
        content = {
            job.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BGColor)
                        .padding(bottom = 55.dp),
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
                                                    contentDescription = "Удалить работу",
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
                                    ){
                                        Column() {
                                            Text(
                                                text = "Материалы",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            item.materials?.forEach{
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Start
                                                ) {
                                                    Box(modifier = Modifier.width(120.dp)) {
                                                        Text(
                                                            it.name,
                                                            color = Color.White,
                                                            fontSize = 15.sp
                                                        )
                                                    }
                                                    Card(shape = RoundedCornerShape(10.dp), backgroundColor = Color.White) {
                                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.width(50.dp)) {
                                                            Text(
                                                                "${it.count}",
                                                                color = Color.Black,
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                    ){
                                        Column() {
                                            Text(
                                                text = "Рабочие",
                                                color = TextJobItem,
                                                fontSize = 12.sp
                                            )
                                            item.employers?.forEach{
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
//                                                    verticalArrangement = Arrangement.CenterVertically,
//                                                    horizontalAlignment = Alignment.Start
                                                ) {
                                                    Box(
                                                        contentAlignment = Alignment.CenterStart
                                                    ) {
                                                        Text(
                                                            text = "${it.post}: ${it.lastName} ${it.firstName} ${it.secondName}",
                                                            color = Color.White,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    }
                                                }
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                    )
                                    {
                                        Button(onClick = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                jobApi.finishTaskToJob(item.id, job.id)
                                                endList.value = jobApi.getAllSubTask(jobId)
                                            }

                                        }) {

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


