package com.example.myapplication.view.map

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.data.remote.response.StoryResponse
import com.example.myapplication.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    private var repo: StoryRepository=repository
    private val _listItem = MutableLiveData<List<ListStoryItem>?>()
    val listItem: MutableLiveData<List<ListStoryItem>?> = _listItem
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init{
        getStoriesWithLocation()
    }
    fun getStoriesWithLocation(){
        viewModelScope.launch {
            fetchStories()
        }
    }
    suspend fun fetchStories(){
        try {
            _isLoading.value = true
            val story = repo.getStoriesWithLocation()
            _listItem.value = story.listStory as List<ListStoryItem>?
        } catch (e: Exception) {
            Log.e(TAG, "onFailure: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
}