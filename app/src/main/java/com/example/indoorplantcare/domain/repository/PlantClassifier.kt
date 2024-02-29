package com.example.indoorplantcare.domain.repository

import android.graphics.Bitmap
import com.example.indoorplantcare.domain.model.Classification

interface PlantClassifier {
    fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification>

}