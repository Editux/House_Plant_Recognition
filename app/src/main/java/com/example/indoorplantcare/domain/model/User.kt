package com.example.indoorplantcare.domain.model

import com.google.firebase.firestore.QueryDocumentSnapshot


data class User(val id: String?,
                 val userId: String,
                 val displayName: String,
                 ){

    fun fromDocument(data: QueryDocumentSnapshot): User {
        return User(
            id = data.id,
            userId = data.get("user_id") as String,
            displayName = data.get("display_name") as String,
        )
    }
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_Id" to this.userId,
            "displayName" to this.displayName,
            )
    }
}