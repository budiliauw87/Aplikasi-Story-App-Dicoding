package com.liaudev.storydicoding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mysubmission_intermediate.Model.UserModel
import com.liaudev.storydicoding.data.local.LocalDataSource
import com.liaudev.storydicoding.data.local.preference.UserPreference
import com.liaudev.storydicoding.data.remote.RemoteDataSource
import com.liaudev.storydicoding.data.remote.response.LoginResponse
import com.liaudev.storydicoding.data.remote.response.RegisterResponse
import com.liaudev.storydicoding.vo.Resource
import kotlinx.coroutines.flow.Flow


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class StoryRepository private constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : DataSource {

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(localDataSource,remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }


    override fun loginAccount(
        email: String,
        password: String
    ): Flow<Resource<LoginResponse>> = remoteDataSource.loginAccount(email, password)

    override fun registerAccount(
        email: String,
        username: String,
        password: String
    ): Flow<Resource<RegisterResponse>> =
        remoteDataSource.registerAccount(email, username, password)

    override fun getUser(): Flow<UserModel> = localDataSource.getUser()

    override suspend fun logOut() = localDataSource.logOut()

    override suspend fun saveUser(user: UserModel) = localDataSource.saveUser(user)




}