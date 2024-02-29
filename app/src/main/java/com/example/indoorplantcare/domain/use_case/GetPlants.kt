package com.example.indoorplantcare.domain.use_case

import com.example.indoorplantcare.domain.repository.PlantRepository

class GetPlants(private val repo: PlantRepository) {
    operator fun invoke()= repo.getPlantsFromFirestore()
}