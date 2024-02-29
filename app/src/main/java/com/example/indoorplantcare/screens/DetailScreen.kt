package com.example.indoorplantcare.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.indoorplantcare.domain.model.Response.*
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.screens.components.ProgressBar
import com.example.indoorplantcare.screens.home.PlantViewModel
import com.example.indoorplantcare.ui.theme.TealDark
import com.example.indoorplantcare.ui.theme.TealPrimary
import com.example.indoorplantcare.utils.Utils


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsScreen(navController: NavController, plantId: String?, viewModel: PlantViewModel = hiltViewModel()) {
    var expanded = remember { mutableStateOf(false) }
    val plantResponse = viewModel.plantResponse
    val categoryResponse = viewModel.categoryResponse
    val mContext = LocalContext.current


    Scaffold(
        topBar = {
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
                    Spacer(modifier = Modifier.width(120.dp))
                    Text(text = "Indoor Plant Care", fontWeight = FontWeight.Bold, color = Color.White,)
                    Spacer(modifier = Modifier.width(115.dp))
                    IconButton(
                        onClick = {
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
                                navController.navigate(Screen.Edit.route+"/$plantId")

                            }) {
                                Text("Edit")
                            }

                            Divider()

                            DropdownMenuItem(onClick = {
                                expanded.value = false

                                viewModel.openDialogState.value = true

                            }) {
                                Text("Delete")
                            }
                        }
                    }

                }
            }
        }

    ) {
        if (viewModel.openDialogState.value) {
            presentDialogue(plantId = plantId, mContext = mContext)
        }
        when (plantResponse) {
            is Loading -> ProgressBar()
            is Success -> Column( modifier= Modifier.verticalScroll(rememberScrollState())) {
                val newPlant = plantResponse.data.firstOrNull{ plant ->
                    plant.id == plantId
                }

                if (newPlant != null) {
                    InfoHeader(image = newPlant.image)
                    InfoTitle(name = newPlant.name, location = newPlant.room_location)
                    when (categoryResponse) {
                        is Loading -> ProgressBar()
                        is Success -> Column() {
                            val category = categoryResponse.data.firstOrNull(){ category ->
                                category.category_id == newPlant!!.plant_category_id
                            }
                            InfoContent(water = category?.water_schedule, sun = category?.sunlight, description = category?.description)
                        }
                        is Error -> Log.d("Category:", "Category didn't load")
                    }


                } else {
                    Text("Plant Not Found");
                }

            }




            is Error -> Utils.printError(plantResponse.e?.message.toString())
        }

    }


}

@Composable
private fun InfoHeader(image:String?){
    Image(
        painter = rememberAsyncImagePainter(model = image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(195.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun InfoTitle(name:String?, location:String?){
    Column(modifier = Modifier.padding(start = 8.dp, end =8.dp,bottom=4.dp,top= 16.dp)) {
        Text(
            text = name.toString(),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.SemiBold
        )
    }
    Row(Modifier.padding(start = 8.dp, end =8.dp,bottom=6.dp,top= 4.dp)){
        Text(
            text = "Location :",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = location.toString(),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Light
        )
    }
    Divider(thickness = 2.dp, startIndent = 8.dp, modifier = Modifier
        .background(color = TealDark))


}
@Composable
private fun InfoContent(water:String?, sun:String?, description:String?) {
    Column(modifier = Modifier.padding(start = 4.dp, end = 2.dp)) {

            Text(
                text = "Watering Schedule :",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = water.toString(),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal
            )


            Text(
                text = "Recommended sunlight:",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = sun.toString(),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal
            )
        Text(
            text = "About the plant:",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold
        )


        Text(
            text = description.toString(),
            style = MaterialTheme.typography.h5,
            fontWeight= FontWeight.Normal
        )


    }
}
@Composable
private fun presentDialogue(viewModel: PlantViewModel = hiltViewModel(), plantId: String?, mContext: Context) {
    if (viewModel.openDialogState.value) {
        AlertDialog(

            onDismissRequest = {
                viewModel.openDialogState.value = false
            },
            title = { Text("Are you sure want to delete this plant information ?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.openDialogState.value = false
                    viewModel.deletePlant(plantId.toString())
                    showDeleteMessage(mContext)
                })
                { Text(text = "Yes") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.openDialogState.value = false }) {
                    Text(text = "No")
                }
            }

        )
    }
}

private fun showDeleteMessage(mContext: Context){

    Toast.makeText(mContext, "Plant is deleted", Toast.LENGTH_SHORT).show()
}








