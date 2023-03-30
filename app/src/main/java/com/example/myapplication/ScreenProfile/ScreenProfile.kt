package com.example.myapplication.ScreenProfile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BGColor
import com.example.myapplication.ui.theme.NavColor
import com.example.myapplication.ui.theme.Red
import com.example.myapplication.R

@Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenProfile() {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BGColor)
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)){
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "ПРОФИЛЬ",
                        color = NavColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                }

            }
        }
    )
}