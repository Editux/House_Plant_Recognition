package com.example.indoorplantcare.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.indoorplantcare.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        displayName: String,
        home: () -> Unit,
    ){
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    //val displayName = task.result?.user?.email.toString().split('@')[0]
                    createUser(displayName)
                    // Log.d("TAG", "ModelView::Success!!!-->: ${task.result?.user?.email}")
                    home()
                }
                _loading.value = false
            }
        }
    }

    private fun createUser(displayName: String) {
        val userId = auth.currentUser?.uid
      val user = User(
          userId = userId!!,
          displayName = displayName,
          id = null).toMap()
        //Log.d("MUser", "createUser: ${user.toMap()}")
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId).set(user)

        }

    }
}