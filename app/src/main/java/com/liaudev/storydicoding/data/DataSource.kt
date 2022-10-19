package com.liaudev.storydicoding.data

import androidx.lifecycle.LiveData
import com.example.mysubmission_intermediate.Model.UserModel
import com.liaudev.storydicoding.data.remote.response.LoginResponse
import com.liaudev.storydicoding.data.remote.response.RegisterResponse
import com.liaudev.storydicoding.vo.Resource
import kotlinx.coroutines.flow.Flow


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
interface DataSource {
    fun loginAccount(
        email: String,
        password: String
    ): Flow<Resource<LoginResponse>>

    fun registerAccount(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<RegisterResponse>>

    fun getUser(): Flow<UserModel>
    suspend fun logOut()
    suspend fun saveUser(user: UserModel)
}