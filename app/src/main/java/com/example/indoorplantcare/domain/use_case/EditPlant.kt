package com.example.indoorplantcare.domain.use_case

import com.example.indoorplantcare.domain.repository.PlantRepository

class EditPlant(private val repo: PlantRepository) {
    suspend operator fun invoke(
        plantId:String,
        plant_category_id:String,
        name:String,
        category:String,
        room_location:String
    )=repo.editPlantToFirestore(plantId,plant_category_id,name,category, room_location)
}