package com.example.myapplication.view.signup

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.UserRepository

class SignUpViewModel(private val userRepository:  UserRepository) : ViewModel(){
    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}