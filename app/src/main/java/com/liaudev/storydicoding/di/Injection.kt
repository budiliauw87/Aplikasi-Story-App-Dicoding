package com.liaudev.storydicoding.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.liaudev.storydicoding.data.StoryRepository
import com.liaudev.storydicoding.data.local.LocalDataSource
import com.liaudev.storydicoding.data.local.preference.UserPreference
import com.liaudev.storydicoding.data.remote.RemoteDataSource
import com.liaudev.storydicoding.network.ApiConfig


/**
 * Created by Budiliauw87 on 2022-10-19.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("story_settings")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val localSource = LocalDataSource.getInstance(userPreference)

        val apiService = ApiConfig.getApiService()
        val remoteSource = RemoteDataSource.getInstance(apiService)

        return StoryRepository.getInstance(localSource,remoteSource)
    }
}