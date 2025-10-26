package com.example.myapplication.view.liststory

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.pref.UserPreference
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.data.remote.retrofit.ApiConfig
import com.example.myapplication.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListStoryViewModel(userRepository: UserRepository,storyRepository:StoryRepository) : ViewModel(){
    private var repo: StoryRepository=storyRepository
    private var repoUser=userRepository
    private val _listItem = MutableLiveData<List<ListStoryItem>?>()
    val listItem: MutableLiveData<List<ListStoryItem>?> = _listItem
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    init {
        getStory()
    }
    fun getStory() {
        viewModelScope.launch {
            repoUser.getSession().collect { user ->
                val token = user.token
                // gunakan token di sini
                fetchStories(token)
            }
        }
    }
    private suspend fun fetchStories(token: String) {
        try {
            _isLoading.value = true
            // panggil API langsung (tanpa enqueue)
            val story = repo.getStories(token)
            _listItem.value = story.listStory as List<ListStoryItem>?
        } catch (e: Exception) {
            Log.e(TAG, "onFailure: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

}