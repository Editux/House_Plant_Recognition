package com.example.indoorplantcare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.indoorplantcare.screens.CreateScreen
import com.example.indoorplantcare.screens.CreateScreenPlant
import com.example.indoorplantcare.screens.DetailsScreen
import com.example.indoorplantcare.screens.EditScreen
import com.example.indoorplantcare.screens.HomeScreen
import com.example.indoorplantcare.screens.PlantAnalysisScreen
import com.example.indoorplantcare.screens.login.LoginScreen
import com.example.indoorplantcare.screens.notification.ReminderScreen
import com.example.indoorplantcare.screens.register.Register

@Composable
fun SetupNavGraph(navController: NavController){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
           HomeScreen(navController=navController)
        }
        composable(route= Screen.Register.route){
            Register(navController = navController)
        }
        composable(route= Screen.Login.route){
            LoginScreen(navController = navController)
        }
        composable(Screen.Details.route +"/{plant}",arguments = listOf(navArgument(name = "plant") {type=
            NavType.StringType})){
                backStrackEntry ->
            DetailsScreen(navController = navController,backStrackEntry.arguments?.getString("plant") )
        }
        composable(Screen.Create.route){
            CreateScreen(navController= navController)
        }

        composable(Screen.Edit.route +"/{plant}",arguments = listOf(navArgument(name = "plant") {type=
            NavType.StringType})){
                backStrackEntry ->
            EditScreen(navController = navController,backStrackEntry.arguments?.getString("plant") )
        }
       composable(Screen.Reminder.route){
            ReminderScreen(navController = navController )
        }
        composable(Screen.PlantAnalysis.route){
            PlantAnalysisScreen(navController = navController )
        }
        composable(Screen.CreatePlant.route +"/{category}",arguments = listOf(navArgument(name = "category") {type=
            NavType.StringType})){
                backStrackEntry ->
            CreateScreenPlant(navController = navController,backStrackEntry.arguments?.getString("category") )
        }
    }
}