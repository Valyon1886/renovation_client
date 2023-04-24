package com.example.myapplication.Auth.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenAuthorization(authActivity: AuthActivity, navController: NavController){

    Scaffold() {
        var emailState by remember { mutableStateOf(TextFieldValue()) }
        var passwordState by remember { mutableStateOf(TextFieldValue()) }
        var showPassword by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ){
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
                        text = "Вход",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 10.dp),
                        textAlign = TextAlign.Left
                    )
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        shape = RoundedCornerShape(25.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordState,
                        onValueChange = { passwordState = it },
                        label = { Text("Пароль") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Lock Icon"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    painter = if (showPassword) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24),
                                    contentDescription = if (showPassword) "Show Password" else "Hide Password"
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val email = emailState.text
                            val password = passwordState.text
                            authActivity.signIn(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(
                            text = "Войти",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
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
                    text = "OR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ){
                Column(){
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        onClick = { navController.navigate(RoutesAuth.Phone.route) },
                        elevation = 5.dp
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center){
                                Icon(Icons.Default.Phone, contentDescription = "Телефон")
                            }
                            Box(modifier = Modifier.weight(4f), contentAlignment = Alignment.CenterStart){
                                Text(
                                    text = "Войти с помощью номера телефона",
                                    fontSize = 17.sp
                                )
                            }

                        }
                    }
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        onClick = { authActivity.signInWithGoogle() },
                        elevation = 5.dp
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center){
                                Icon(painter = painterResource(id = R.drawable.icons8_google), contentDescription = "GoogleAcc", Modifier.size(25.dp))
                            }
                            Box(modifier = Modifier.weight(4f), contentAlignment = Alignment.CenterStart){
                                Text(
                                    text = "Войти с помощью Google Account",
                                    fontSize = 17.sp
                                )
                            }

                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Вы не зарегистрированы?",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = { navController.navigate(RoutesAuth.Registration.route) }
                            ),
                        text = "Зарегистрироваться",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Blue
                    )
                }
            }
        }
    }
}