package com.example.indoorplantcare.screens

import android.annotation.SuppressLint
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.indoorplantcare.data.TfLitePlantClassifier
import com.example.indoorplantcare.domain.model.Classification
import com.example.indoorplantcare.navigation.Screen
import com.example.indoorplantcare.screens.components.CameraPreview
import com.example.indoorplantcare.screens.components.ImageAnalyzer
import com.example.indoorplantcare.ui.theme.TealPrimary

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantAnalysisScreen(navController: NavController) {
    val context = LocalContext.current
    var classifications by remember {
        mutableStateOf(emptyList<Classification>()) }



    // Preparing Image Analyzer
    val imageAnalyzer =  remember {
        ImageAnalyzer(
            classifier = TfLitePlantClassifier(
                context = context
            ),
            onClassificationResults = {
                classifications = it
            }
        )
    }

    val cameraPreviewController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                imageAnalyzer
            )
        }
    }
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
                    Text(text = "Camera", fontWeight = FontWeight.Bold, color = Color.White,)
                    Spacer(modifier = Modifier.width(115.dp))

                }

            }

        }

    ) {
Column(modifier = Modifier.padding(all = 50.dp)) {

        classifications.forEach {
            Row() {
                Text(
                    text = it.detectedObjectName,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable{
                            navController.navigate(Screen.CreatePlant.route+ "/${it.detectedObjectName}")
                        }
                    )
            }

    }
        CameraPreview(
            controller = cameraPreviewController,
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)
        )


    }}}









