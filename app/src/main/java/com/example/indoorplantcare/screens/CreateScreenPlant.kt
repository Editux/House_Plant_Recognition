package com.example.indoorplantcare.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.screens.home.PlantViewModel
import com.example.indoorplantcare.ui.theme.TealDark
import com.example.indoorplantcare.ui.theme.TealPrimary


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CreateScreenPlant(navController:NavController, category: String?, viewModel: PlantViewModel = hiltViewModel() ) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        selectedImage = uri
    }
    var name by remember { mutableStateOf("") }
    var room_location by remember { mutableStateOf("") }
    val context = LocalContext.current


    val listItems = arrayOf("None","Aloe", "Cactus","Chinese Money Tree","Snake Plant","Lavender","Random plant")
    var category_id =listItems.indexOf(category).toString()

    var expanded by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar ={
        //TopBar
        TopAppBar(backgroundColor = TealPrimary,
            elevation = 0.dp ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    tint= Color.White ,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Spacer(modifier = Modifier.width(150.dp))
                Text(text = "Add Plant", fontWeight = FontWeight.Bold, color = Color.White)
            }

        }
    }
    ) {
        Column(modifier= Modifier
            .padding(all = 50.dp)
            .verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(10.dp))
            ImageContent(selectedImage) {
                launcher.launch("image/*")
            }
            Spacer(Modifier.height(20.dp))
            TextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text(text = "Your Plant Name") },
                placeholder = { Text(text = "Title") },
                colors = TextFieldDefaults.textFieldColors(focusedLabelColor = TealDark,
                )
            )
            Spacer(Modifier.height(15.dp))
            TextField(
                value = room_location,
                onValueChange = {
                    room_location = it
                },
                label = { Text(text = "Plant location") },
                placeholder = { Text(text = "Plant location") },
                colors = TextFieldDefaults.textFieldColors(focusedLabelColor = TealDark  )

            )
            Spacer(Modifier.height(15.dp))
            Text(
                text= category.toString(),
            )
            Spacer(Modifier.height(15.dp))

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = TealDark,
                    contentColor = Color.White ),
                onClick = {
                    viewModel.addPlant(category_id,selectedImage.toString(), name,
                        category.toString(),room_location)
                    Toast.makeText(context,"Plant is added to the database", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Home.route)
                },
            ) {
                Text(text = "Save")
            }

        }



    }
}
@Composable
private fun ImageContent(
    selectedImage: Uri? = null,
    onImageClick: () -> Unit
) {
    if (selectedImage != null)
        Image(
            painter = rememberAsyncImagePainter(model = selectedImage),
            contentDescription = "Selected image",
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .clickable {
                    onImageClick()
                })
    else
        OutlinedButton(onClick = onImageClick, colors = ButtonDefaults.buttonColors(
            contentColor =  TealDark, backgroundColor = Color.White ),) {
            Text(text = "Choose Image")
        }

}
