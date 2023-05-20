package com.example.myapplication.ScreenDocx

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Entity.MainMaterial
import com.example.myapplication.Entity.User
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.Retrofit.DocumentApi
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenDocx(documentApi: DocumentApi, mainActivity: MainActivity, user: User) {
    var listDocument by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(true) {
        listDocument = documentApi.getAllFileNames(user.id)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ДОКУМЕНТЫ",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                backgroundColor = NavColor
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
            ){
                LazyColumn {
                    itemsIndexed(listDocument) { _, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            backgroundColor = NavColor,
                            shape = RoundedCornerShape(10.dp),
                            elevation = 10.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier.weight(6f)
                                    ) {
                                        Text(
                                            text = item,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                CoroutineScope(Dispatchers.IO).launch {

                                                    val response = documentApi.downloadFile(item)
                                                    val inputStream = response.body()?.byteStream()
                                                    val file = File("/storage/emulated/0/Download/", "$item.docx")
                                                    val outputStream = FileOutputStream(file)

                                                    inputStream?.use { input ->
                                                        outputStream.use { output ->
                                                            input.copyTo(output)
                                                        }
                                                    }

                                                    mainActivity.runOnUiThread{
                                                        Toast.makeText(mainActivity, "Отчет загружен на Ваше устройство!", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        ){
                                            Icon(
                                                painterResource(id = R.drawable.baseline_arrow_circle_down_24),
                                                contentDescription = "Скачать документ",
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
    )
}