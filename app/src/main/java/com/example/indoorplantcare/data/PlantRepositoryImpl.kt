package com.example.indoorplantcare.data


import com.example.indoorplantcare.domain.model.Plant
import com.example.indoorplantcare.domain.model.Plant_Category
import com.example.indoorplantcare.domain.model.Response.Success
import com.example.indoorplantcare.domain.repository.AddPlantResponse
import com.example.indoorplantcare.domain.repository.CategoryResponse
import com.example.indoorplantcare.domain.repository.DeletePlantResponse
import com.example.indoorplantcare.domain.repository.EditPlantResponse
import com.example.indoorplantcare.domain.repository.PlantRepository
import com.example.indoorplantcare.domain.repository.PlantResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantRepositoryImpl @Inject constructor(
    private val userRef: CollectionReference,

): PlantRepository {
    override fun getPlantsFromFirestore() = callbackFlow {

      val user = Firebase.auth.currentUser!!.uid

        val snapshotListener = userRef.document(user).collection("plants").orderBy("name").addSnapshotListener{ snapshot, e ->

            val plantResponse = if (snapshot != null) {
                val plant = snapshot.toObjects(Plant::class.java)

               Success(plant)
            } else {
                Error(e?.message ?: e.toString())
            }
            trySend(plantResponse as PlantResponse).isSuccess
        }
       awaitClose {
           snapshotListener.remove()
       }


   }

    override fun getCategoriesFromFirestore(): Flow<CategoryResponse> = callbackFlow {
        val db = Firebase.firestore

        val snapshotListener = db.collection("plantcategory").orderBy("category_name").addSnapshotListener{ snapshot, e ->

            val response = if (snapshot != null) {
                val plant = snapshot.toObjects(Plant_Category::class.java)

                Success(plant)
            } else {
                Error(e?.message ?: e.toString())
            }
            trySend(response as CategoryResponse).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }



    override suspend fun addPlantToFirestore (
        plant_category_id: String,
        image: String,
        name: String,
        category: String,
        room_location: String,
    ):AddPlantResponse {
        try {
            val user = Firebase.auth.currentUser?.uid
            val db = Firebase.firestore
            val id = db.collection("users/$user/plants").document().id
            val plant = Plant(
                id = id,
                image = image,
                plant_category_id = plant_category_id,
                name = name,
                category = category,
                room_location = room_location,
            )

            db.collection("users/$user/plants").document(id).set(plant).await()
            //Success(true as AddPlantResponse)
        } catch (e: Exception) {
            Error(e.message ?: e.toString())
        }
        return Success(true )
    }

    override suspend fun editPlantToFirestore(
        plant_id: String,
        plant_category_id: String,
        name: String,
        category: String,
        room_location: String,
    ): EditPlantResponse {
        val user = Firebase.auth.currentUser?.uid
        val db = Firebase.firestore

        try {

            val plant = Plant(
                id =plant_id,
               plant_category_id = plant_category_id,
                name=name,
                category= category,
                room_location= room_location,


            )


            val edition =db.collection("users/$user/plants").document(plant_id).update(
                "plant_category_id", plant_category_id,
                "name", name,
                "room_location", room_location).await()
            Success(edition)

        } catch (e: Exception) {
            Error(e.message ?: e.toString())
        }
        return Success(true)
    }

    override suspend fun deletePlantFromFirestore(plantId: String): DeletePlantResponse {
        val user = Firebase.auth.currentUser?.uid
        val db = Firebase.firestore
        try {

           db.collection("users/$user/plants").document(plantId).delete().await()

        } catch (e: Exception) {
            Error(e.message ?: e.toString())
        }
        return Success(true)
    }


//    override fun getCategoryFromFirestore() = callbackFlow {
//
//        val db = Firebase.firestore
//
//        val snapshotListener = db.collection("plantcategory").orderBy("category_name").addSnapshotListener{ snapshot, e ->
//
//            val response = if (snapshot != null) {
//                val plant = snapshot.toObjects(Plant_Category::class.java)
//
//                Success(plant)
//            } else {
//                Error(e?.message ?: e.toString())
//            }
//            trySend(response).isSuccess
//        }
//        awaitClose {
//            snapshotListener.remove()
//        }
    }

//    @SuppressLint("SuspiciousIndentation")
//   override suspend fun addPlantToFirestore(plant_category_id:String, image:String, name:String, category:String, room_location:String) = try {
//        val user = Firebase.auth.currentUser?.uid
//    val db = Firebase.firestore
//            val id = db.collection("users/$user/plants").document().id
//            val plant = Plant(
//                id = id,
//                image= image,
//                plant_category_id = plant_category_id,
//                name=name,
//                category= category,
//                room_location = room_location,)
//
//            db.collection("users/$user/plants").document(id).set(plant).await()
//
//        } catch (e: Exception) {
//            Error(e.message ?: e.toString())
//        }


//    override suspend fun editPlantToFirestore( plant_id:String, plant_category_id:String,name:String, category:String, room_location:String) = flow {
//        val user = Firebase.auth.currentUser?.uid
//        val db = Firebase.firestore
//
//        try {
//            emit(Loading)
//            val plant = Plant(
//                id =plant_id,
//               plant_category_id = plant_category_id,
//                name=name,
//                category= category,
//                room_location= room_location,
//
//
//            )


//            val edition =db.collection("users/$user/plants").document(plant_id).update(
//                "plant_category_id", plant_category_id,
//                "name", name,
//                "room_location", room_location).await()
//            emit(Success(edition))
//
//        } catch (e: Exception) {
//            emit(Error(e.message ?: e.toString()))
//        }
//    }

//    override suspend fun deletePlantFromFirestore(plant_id: String) = flow {
//        val user = Firebase.auth.currentUser?.uid
//        val db = Firebase.firestore
//        try {
//            emit(Loading)
//
//            val deletion= db.collection("users/$user/plants").document(plant_id).delete().await()
//            emit(Success(deletion))
//        } catch (e: Exception) {
//            emit(Error(e.message ?: e.toString()))
//        }
//    }

//
//}