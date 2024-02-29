package com.example.indoorplantcare.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.indoorplantcare.domain.model.Plant
import com.example.indoorplantcare.domain.model.Response.*
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.screens.components.ProgressBar
import com.example.indoorplantcare.screens.home.PlantViewModel
import com.example.indoorplantcare.ui.theme.TealPrimary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen (navController: NavController, viewModel: PlantViewModel = hiltViewModel()) {
    val expanded = remember { mutableStateOf(false) }
    val plantResponse = viewModel.plantResponse
    val auth: FirebaseAuth = Firebase.auth
    val mContext = LocalContext.current

    Scaffold(topBar = {
        //TopBar
        TopAppBar(
            backgroundColor = TealPrimary,
            elevation = 0.dp
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Spacer(modifier = Modifier.width(120.dp))
                Text(text = "House Plant Care", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(115.dp))
                IconButton(onClick = {
                    expanded.value = true // 2
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            navController.navigate(Screen.Create.route)

                        }) {
                            Text("Create")
                        }
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            navController.navigate(Screen.PlantAnalysis.route)

                        }) {
                            Text("Recognition Camera")
                        }
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            navController.navigate(Screen.Reminder.route)
                        }) {
                            Text("Notifications")
                        }
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            auth.signOut()
                            navController.navigate(Screen.Login.route)

                        }) {
                            Text("Log out")
                        }
                    }
                }
            }
        }
    }) {
        when(val plantResponse = viewModel.plantResponse) {
            is Loading -> ProgressBar()
            is Success -> LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()

            ){
                items(items = plantResponse.data){
                    CardList(model = it ) { model ->
                        navController.navigate(Screen.Details.route + "/${model}") }

                }}
            is Error-> print(plantResponse.e)
        }

        }
}
@Composable
fun CardList(model: Plant, onItemClick:(String) -> Unit = {} ){

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
                start = 6.dp,
                end = 6.dp,
            )
            .clickable {
                onItemClick(model.id.toString())

            }
            .fillMaxWidth(),

        elevation = 8.dp

    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = model.image) ,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                Text(
                    text = model.name.toString(),
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(
                            top = 0.dp,
                            bottom = 0.dp,
                            start = 0.dp,
                            end = 8.dp
                        ),


                    style = MaterialTheme.typography.h5

                )
            }
            Text(
                text = "Location : ${model.room_location}",
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .wrapContentWidth(Alignment.Start)
                    .padding(
                        top = 2.dp,
                        bottom = 12.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                style = MaterialTheme.typography.h6
            )

        }
    }

}