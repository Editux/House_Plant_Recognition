package com.example.indoorplantcare.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.indoorplantcare.domain.model.Plant

import com.example.indoorplantcare.domain.model.Response.*
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.screens.components.ProgressBar
import com.example.indoorplantcare.screens.home.PlantViewModel
import com.example.indoorplantcare.ui.theme.TealDark
import com.example.indoorplantcare.ui.theme.TealPrimary

@Composable
fun EditScreen(navController: NavController, articleId: String?, viewModel: PlantViewModel = hiltViewModel()) {
    val plantResponse = viewModel.plantResponse
    when (plantResponse) {
        is Loading -> ProgressBar()
        is Success-> ContentScreen(navController = navController,plantResponse.data,
            articleId.toString()
        )

        else -> {}
    }

}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ContentScreen(navController: NavController, model: List<Plant>, plantId:String, viewModel: PlantViewModel = hiltViewModel()){
    val context = LocalContext.current
    val newPlant = model.firstOrNull { plant->
        plant.id == plantId
    }
    if (newPlant != null) {
        var name by remember { mutableStateOf(newPlant.name) }
        var location by remember { mutableStateOf(newPlant.room_location) }
        var category_id by remember { mutableStateOf("") }
        val listItems = arrayOf("None","Aloe", "Cactus","Chinese Money Tree","Snake Plant","Lavender","Random plant")
        var selectedItem by remember {
            mutableStateOf(listItems[0])
        }
        var expanded by remember {
            mutableStateOf(false)
        }
        Scaffold(topBar = {
            //TopBar
            TopAppBar(
                backgroundColor = TealPrimary,
                elevation = 0.dp
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        })
                    Spacer(modifier = Modifier.width(150.dp))
                    Text(text = "Edit Plant", fontWeight = FontWeight.Bold, color = Color.White)
                }

            }
        }
        ) {
            Column(modifier = Modifier.padding(all = 50.dp)) {
                Spacer(Modifier.height(10.dp))

                Spacer(Modifier.height(20.dp))
                TextField(
                    value = name.toString(),
                    onValueChange = {
                        name = it
                    },
                    label = { Text(text = "Name:") },
                    placeholder = { Text(text = "Name:") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedLabelColor = TealDark,
                    )
                )
                Spacer(Modifier.height(15.dp))
                TextField(
                    value = location.toString(),
                    onValueChange = {
                        location = it
                    },
                    label = { Text(text = "Room location:") },
                    placeholder = { Text(text = "Room location") },
                    colors = TextFieldDefaults.textFieldColors(focusedLabelColor = TealDark )

                )
                Spacer(Modifier.height(15.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    // text field
                    TextField(
                        value = selectedItem,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Category") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    // menu
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listItems.forEach { selectedOption ->
                            // menu item
                            DropdownMenuItem(onClick = {
                                selectedItem = selectedOption
                                category_id = listItems.indexOf(selectedItem).toString()
                                Toast.makeText(context, selectedOption, Toast.LENGTH_SHORT).show()
                                expanded = false
                            }) {
                                Text(text = selectedOption)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(40.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = TealDark,
                        contentColor = Color.White
                    ),
                    onClick = { viewModel.editPlant(plantId,category_id,
                        name.toString(),
                        selectedItem,
                        location.toString(),
                    )
                        Toast.makeText(context,"The plant information is updated", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route)         },
                ) {
                    Text(text = "Save")
                }

            }
        }
    }
}