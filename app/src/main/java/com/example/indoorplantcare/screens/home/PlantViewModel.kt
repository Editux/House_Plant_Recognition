package com.example.indoorplantcare.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indoorplantcare.domain.model.Response
import com.example.indoorplantcare.domain.repository.AddPlantResponse
import com.example.indoorplantcare.domain.repository.CategoryResponse
import com.example.indoorplantcare.domain.repository.DeletePlantResponse
import com.example.indoorplantcare.domain.repository.EditPlantResponse
import com.example.indoorplantcare.domain.repository.PlantResponse
import com.example.indoorplantcare.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    var plantResponse by mutableStateOf<PlantResponse>(Response.Loading)
        private set

    var categoryResponse by mutableStateOf<CategoryResponse>(Response.Loading)
        private set

    var addPlantResponse by mutableStateOf<AddPlantResponse>(Response.Success(false))
        private set

    var editPlantResponse by mutableStateOf<EditPlantResponse>(Response.Success(false))
        private set
    var deletePlantResponse by mutableStateOf<DeletePlantResponse>(Response.Success(false))
        private set


    var openDialogState = mutableStateOf(false)

    init {
        getPlants()
        getCategories()

    }

    private fun getPlants() = viewModelScope.launch {
        useCases.getPlants().collect { response ->
            plantResponse = response
        }
    }


    private fun getCategories() {
        viewModelScope.launch {
            useCases.getCategory().collect() { category ->
                categoryResponse = category
            }
        }
    }


    fun addPlant(
        plant_category_id: String,
        image: String,
        name: String,
        category: String,
        room_location: String,
    ) {
        viewModelScope.launch {
            addPlantResponse=Response.Loading
            addPlantResponse = useCases.addPlant(plant_category_id, image, name, category, room_location)

        }
    }

    fun editPlant(
        plant_id: String,
        plant_category_id: String,
        name: String,
        category: String,
        room_location: String
    ) {
        viewModelScope.launch {
            editPlantResponse= Response.Loading
            editPlantResponse=useCases.editPlant(plant_id, plant_category_id, name, category, room_location)
                }

    }

    fun deletePlant(plant_id: String) {
        viewModelScope.launch {
            deletePlantResponse = Response.Loading
            deletePlantResponse= useCases.deletePlant(plant_id)
            }
        }
    }





