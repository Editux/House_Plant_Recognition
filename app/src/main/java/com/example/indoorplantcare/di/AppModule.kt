package com.example.indoorplantcare.di
import com.example.indoorplantcare.data.PlantRepositoryImpl
import com.example.indoorplantcare.domain.repository.PlantRepository
import com.example.indoorplantcare.domain.use_case.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseFirestore()= Firebase.firestore


    @Provides
    fun provideUserRef(
        db:FirebaseFirestore

   )=db.collection("users")

   @Provides
    fun provideUserRepository(
       userRef: CollectionReference
 ): PlantRepository = PlantRepositoryImpl(userRef)

    @Provides
    fun provideUseCases(
        repo: PlantRepository
    ) = UseCases(
        getPlants = GetPlants(repo),
        getCategory= GetCategory(repo),
        addPlant = AddPlant(repo),
        editPlant= EditPlant(repo),
        deletePlant = DeletePlant(repo)


    )




}