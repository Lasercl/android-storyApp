package com.example.myapplication.data.repository

import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.pref.UserPreference
import com.example.myapplication.data.remote.response.StoryResponse
import com.example.myapplication.data.remote.retrofit.ApiService



class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getStories(token: String): StoryResponse {
        return apiService.getStories()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService,userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
