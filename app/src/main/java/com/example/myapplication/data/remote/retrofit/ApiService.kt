package com.example.myapplication.data.remote.retrofit

import com.example.myapplication.data.remote.response.LoginResponse
import com.example.myapplication.data.remote.response.RegisterResponse
import com.example.myapplication.data.remote.response.StoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
//    @GET("events")
//    fun getEvent(
//    ): Call<RegisterResponse>
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
    @Field("name") name: String,
    @Field("email") email: String,
    @Field("password") password: String
    ): RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse
}