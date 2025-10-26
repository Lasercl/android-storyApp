package com.example.myapplication.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.repository.StoryRepository
import com.example.myapplication.di.Injection
import com.example.myapplication.view.liststory.ListStoryViewModel
import com.example.myapplication.view.login.LoginViewModel
import com.example.myapplication.view.main.MainViewModel
import com.example.myapplication.view.signup.SignUpViewModel


class ViewModelFactory(private val repository: UserRepository,private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(repository,storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val userRepo = Injection.provideUserRepository(context)
                    val storyRepo = Injection.provideStoryRepository(context)
                    INSTANCE = ViewModelFactory(userRepo, storyRepo)                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}