package com.example.indoorplantcare.domain.model

data class Classification(
    val detectedObjectName: String,
    val confidenceScore: Float
)