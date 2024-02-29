package com.example.indoorplantcare.domain.use_case

data class UseCases(
    val getPlants: GetPlants,
    val addPlant: AddPlant,
    val deletePlant: DeletePlant,
    val getCategory: GetCategory,
    val editPlant: EditPlant,
)

