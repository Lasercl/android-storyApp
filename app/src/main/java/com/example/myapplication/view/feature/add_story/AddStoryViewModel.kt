package com.example.myapplication.view.feature.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.File
import com.example.myapplication.data.Result
import com.example.myapplication.data.remote.response.AddStoryResponse
import com.example.myapplication.data.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddStoryViewModel(private val repository: StoryRepository): ViewModel() {
    private val _uploadResult = MutableLiveData<Result<String>>()
    val uploadResult: LiveData<Result<String>> = _uploadResult

    fun uploadStory(file: File, description: String) {
        _uploadResult.value = Result.Loading

        viewModelScope.launch {
            try {
                val response = repository.uploadStory(file, description)
                _uploadResult.value = Result.Success(response.message) as Result<String>?
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
                _uploadResult.value = errorResponse.message?.let { Result.Error(it) }
            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }
}