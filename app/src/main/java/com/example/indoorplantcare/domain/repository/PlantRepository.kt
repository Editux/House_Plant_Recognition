package com.example.indoorplantcare.domain.repository

import com.example.indoorplantcare.domain.model.Plant
import com.example.indoorplantcare.domain.model.Plant_Category
import com.example.indoorplantcare.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias Plants = List<Plant>
typealias Category =List<Plant_Category>
typealias CategoryResponse=Response<Category>
typealias PlantResponse = Response<Plants>
typealias AddPlantResponse = Response<Boolean>
typealias EditPlantResponse= Response<Boolean>
typealias DeletePlantResponse = Response<Boolean>

interface PlantRepository {
    fun getPlantsFromFirestore(): Flow<PlantResponse>

    fun getCategoriesFromFirestore():Flow<CategoryResponse>

    suspend fun addPlantToFirestore(plant_category_id:String,image:String,name:String, category:String, room_location:String): AddPlantResponse

    suspend fun editPlantToFirestore(plant_id:String, plant_category_id:String,name:String, category:String, room_location:String): EditPlantResponse

    suspend fun deletePlantFromFirestore(plantId: String): DeletePlantResponse

}