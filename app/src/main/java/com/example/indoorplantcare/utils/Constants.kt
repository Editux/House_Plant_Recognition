package com.example.indoorplantcare.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Constants {
        const val TAG = "AppTag"


        const val BASE_URL = "https://house-plants2.p.rapidapi.com/"

        //Firestore
        const val PLANTS = "plants"
        const val NAME = "name"

        //Actions
        const val ADD_PLANT = "Add a plant"
        const val DELETE_PLANT = "Delete a Plant."

        //Buttons
        const val ADD = "Add"
        const val DISMISS = "Dismiss"

        //Placeholders
        const val PLANT_ROOM = "Plant room"
        const val USER_DISPLAY_NAME = "User display name"
    }
