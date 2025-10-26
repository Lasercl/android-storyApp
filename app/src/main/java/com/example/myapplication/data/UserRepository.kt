package com.example.myapplication.data


import androidx.lifecycle.liveData
import com.example.myapplication.data.pref.UserModel
import com.example.myapplication.data.pref.UserPreference
import com.example.myapplication.data.remote.retrofit.ApiService
import com.example.myapplication.data.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val appExecutors: AppExecutors
) {
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
            val token = response.loginResult?.token ?: ""
            val userEmail = email
            val user = UserModel(userEmail, token, true)
            userPreference.saveSession(user)

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService,appExecutors)
            }.also { instance = it }
    }
}