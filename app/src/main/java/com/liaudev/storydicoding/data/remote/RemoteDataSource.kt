package com.liaudev.storydicoding.data.remote

import com.liaudev.storydicoding.data.remote.response.LoginResponse
import com.liaudev.storydicoding.data.remote.response.RegisterResponse
import com.liaudev.storydicoding.network.ApiService
import com.liaudev.storydicoding.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class RemoteDataSource private constructor(private val apiService: ApiService) {
    fun loginAccount(
        email: String,
        password: String
    ): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val user = mapOf(
                    "email" to email,
                    "password" to password
                )
                val response = apiService.loginUser(user)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun registerAccount(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val user = mapOf(
                    "email" to email,
                    "name" to username,
                    "password" to password
                )
                val response = apiService.registerUser(user)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}