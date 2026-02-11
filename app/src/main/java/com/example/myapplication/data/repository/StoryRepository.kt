package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.myapplication.data.paging.StoryPagingSource
import com.example.myapplication.data.pref.UserPreference
import com.example.myapplication.data.remote.response.AddStoryResponse
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.data.remote.response.StoryResponse
import com.example.myapplication.data.remote.retrofit.ApiConfig
import com.example.myapplication.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class StoryRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun apiService(): ApiService{
        val user = userPreference.getSession().first()
        return ApiConfig.getApiService(user.token)
    }

     suspend fun getStories(): LiveData<PagingData<ListStoryItem>> {
       var api:  ApiService=apiService()

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(api)
            }
        ).liveData

    }
    suspend fun getStoriesWithLocation(): StoryResponse{
        return apiService().getStoriesWithLocation()
    }
    suspend fun uploadStory(file: File, description: String): AddStoryResponse {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        return apiService().uploudStory(multipartBody, requestBody)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository( userPreference)
            }.also { instance = it }
    }
}
