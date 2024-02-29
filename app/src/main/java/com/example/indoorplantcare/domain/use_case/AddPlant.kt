package com.example.indoorplantcare.domain.use_case

import com.example.indoorplantcare.domain.repository.PlantRepository

class AddPlant( private val repo: PlantRepository) {
    suspend operator fun invoke(
        plant_category_id:String,
        image:String,
        name:String,
        category:String,
        room_location:String,
    )= repo.addPlantToFirestore(plant_category_id,image,name,category, room_location)

}