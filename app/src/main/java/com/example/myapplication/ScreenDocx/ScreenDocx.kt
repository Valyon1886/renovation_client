package com.example.myapplication.ScreenDocx

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenDocx() {
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

            }
        }
    )
}