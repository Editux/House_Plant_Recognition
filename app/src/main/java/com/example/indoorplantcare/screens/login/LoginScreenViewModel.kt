package com.example.indoorplantcare.screens.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indoorplantcare.utils.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel:ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch {
        Log.d("Call", "Calling = createUserWithEmailAndPassword: ")
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    home()

                }else {
                    Log.d("Failed", "Failed!!: ${task.result}")
                }
            }
        }catch (e: Exception){
            Log.d("EXC", "signInWithEmailAndPassword: ${e.message}")
        }
    }
}