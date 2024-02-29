package com.example.indoorplantcare.domain.use_case

import com.example.indoorplantcare.domain.repository.PlantRepository

class GetCategory(private val repo: PlantRepository) {
    operator fun invoke()= repo.getCategoriesFromFirestore()
}