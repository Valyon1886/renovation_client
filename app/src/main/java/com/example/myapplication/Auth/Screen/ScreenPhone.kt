package com.example.myapplication.Auth.Screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Auth.RoutesAuth
import com.example.myapplication.AuthActivity
import com.example.myapplication.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenPhone(authActivity: AuthActivity, navController: NavController){
    Scaffold() {
        var phoneState by remember { mutableStateOf(TextFieldValue()) }
        var codeState by remember { mutableStateOf(TextFieldValue()) }
        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    Text(
                        text = "Номер телефона",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 10.dp),
                        textAlign = TextAlign.Left
                    )
                    BasicTextField(
                        value = phoneState,
                        onValueChange = { phoneState = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {

                                phoneState.text.forEachIndexed { index, char ->
                                    DuckieTextFieldCharContainer(
                                        text = char,
                                        isFocused = index == phoneState.text.lastIndex,
                                    )
                                }
                                repeat(12 - phoneState.text.length) {
                                    DuckieTextFieldCharContainer(
                                        text = ' ',
                                        isFocused = false,
                                    )
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AnimatedVisibility(visible = expanded) {
                        OutlinedTextField(
                            value = codeState,
                            onValueChange = { codeState = it },
                            label = { Text("Code") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_vpn_key_24),
                                    contentDescription = "Code Icon"
                                )
                            },
                            shape = RoundedCornerShape(25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(2.7f), contentAlignment = Alignment.Center){
                            Button(
                                onClick = {
                                    expanded = !expanded
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(Color.Blue)
                            ) {
                                Text(
                                    text = "Отправить код",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Box(modifier = Modifier.weight(0.5f)){ Spacer(modifier = Modifier.height(16.dp)) }
                        Box(modifier = Modifier.weight(2.7f), contentAlignment = Alignment.Center){
                            Button(
                                onClick = {
                                    navController.navigate(RoutesAuth.Authorization.route)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(Color.Blue)
                            ) {
                                Text(
                                    text = "Подтвердить",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3.5f)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun DuckieTextFieldCharContainer(
    modifier: Modifier = Modifier,
    text: Char,
    isFocused: Boolean,
) {
    val shape = remember { RoundedCornerShape(4.dp) }

    Box(
        modifier = modifier
            .size(
                width = 29.dp,
                height = 40.dp,
            )
            .background(
                color = Color(0xFFF6F6F6),
                shape = shape,
            )
            .run {
                if (isFocused) {
                    border(
                        width = 1.dp,
                        color = Color(0xFFFF8300),
                        shape = shape,
                    )
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text.toString())
    }
}