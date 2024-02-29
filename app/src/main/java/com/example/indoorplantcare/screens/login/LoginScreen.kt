package com.example.indoorplantcare.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.indoorplantcare.R
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.ui.theme.TealDark


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel= viewModel()) {

    val scaffoldState = rememberScaffoldState()
    val emailVal = rememberSaveable { mutableStateOf("") }
    val passwordVal = rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_baseline_grass_24_green),
                    contentDescription = "Login Image",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),

                    contentScale = ContentScale.Fit
                )
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Login",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        OutlinedTextField(
                            value = emailVal.value,
                            onValueChange = { emailVal.value = it },
                            label = { Text(text = "Email Address:", color = Color.Black) },
                            placeholder = { Text(text = "Email Address" , color = Color.Black) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color.Black, textColor = Color.Black
                            )
                        )

                        OutlinedTextField(
                            value = passwordVal.value,
                            onValueChange = { passwordVal.value = it },
                            label = { Text(text = "Password:", color = Color.Black) },
                            placeholder = { Text(text = "Password" , color = Color.Black) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color.Black, textColor = Color.Black
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        passwordVisibility.value = !passwordVisibility.value
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_remove_red_eye_24),
                                        contentDescription = "Password Eye",
                                        tint = if (passwordVisibility.value) TealDark else Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                            else PasswordVisualTransformation()
                        )



                        Spacer(modifier = Modifier.padding(10.dp))

                        Text(text=errorMessage, color = Color.Red)

                        Button(colors = ButtonDefaults.buttonColors(backgroundColor = TealDark),
                            onClick = {
                                if (emailVal.value.isEmpty()) {

                                    errorMessage="Please enter the email address!"

                                } else if (passwordVal.value.isEmpty()) {

                                    errorMessage="Please enter password!"

                                } else {

                                    viewModel.signInWithEmailAndPassword(emailVal.value.trim(),
                                        passwordVal.value.trim()
                                    ){
                                        navController?.navigate(Screen.Home.route)
                                    }

                                }
                            }
                        ) {
                            Text(text = "Login", fontSize = 20.sp,color = Color.White)
                        }

                        Row(
                            modifier = Modifier.padding(bottom = 15.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            val text = "Sign up!"
                            Text("New User?")
                            Text(text,
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(Screen.Register.route)

                                    }
                                    .padding(start = 5.dp),

                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondaryVariant)
                        }

                    }
                }
            }

        }
    }

}
