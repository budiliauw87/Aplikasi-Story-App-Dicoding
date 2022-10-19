package com.liaudev.storydicoding.data.local

import com.example.mysubmission_intermediate.Model.UserModel
import com.liaudev.storydicoding.data.local.preference.UserPreference
import kotlinx.coroutines.flow.Flow


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class LocalDataSource private constructor(private val userPreference: UserPreference) {
    fun getUser(): Flow<UserModel>  = userPreference.getUser()
    suspend fun saveUser(userModel: UserModel)  = userPreference.saveUSer(userModel)
    suspend fun logOut() = userPreference.logOut()
}