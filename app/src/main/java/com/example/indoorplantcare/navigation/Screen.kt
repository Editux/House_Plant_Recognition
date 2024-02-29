package com.example.indoorplantcare.navigation

sealed class Screen(val route:String){
    object Splash:Screen("splash_screen")
    object Home:Screen("home_screen")
    object Register:Screen("register_screen")
    object Login:Screen("login_screen")
    object Details:Screen("details_screen")
    object Create:Screen("create_screen")
    object Edit:Screen("edit_screen")
    object Reminder:Screen("reminder_screen")
    object PlantAnalysis:Screen("analysis_screen")
    object CreatePlant:Screen("create_plant_screen")
}
