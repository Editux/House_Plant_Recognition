package com.example.indoorplantcare.domain.use_case

import com.example.indoorplantcare.domain.repository.PlantRepository

class DeletePlant(private val repo: PlantRepository) {
    suspend operator fun invoke(PlantId:String)=repo.deletePlantFromFirestore(plantId = PlantId)
}