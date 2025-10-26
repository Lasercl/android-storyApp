package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.pref.UserPreference
import com.example.myapplication.data.pref.dataStore
import com.example.myapplication.data.remote.retrofit.ApiConfig
import com.example.myapplication.data.remote.retrofit.ApiService
import com.example.myapplication.data.repository.StoryRepository
import com.example.myapplication.data.utils.AppExecutors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(userPreference, apiService, appExecutors)
    }
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}
