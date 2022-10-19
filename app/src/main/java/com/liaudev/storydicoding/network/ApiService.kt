package com.liaudev.storydicoding.network

import com.liaudev.storydicoding.data.remote.response.LoginResponse
import com.liaudev.storydicoding.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
interface ApiService {

    @POST("login")
    suspend fun loginUser(
        @Body user: Map<String, String>
    ): LoginResponse

    @POST("register")
    suspend fun registerUser(
        @Body user: Map<String, String>
    ): RegisterResponse

}