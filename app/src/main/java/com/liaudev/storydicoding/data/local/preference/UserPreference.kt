package com.liaudev.storydicoding.data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mysubmission_intermediate.Model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val LOGGED_KEY = booleanPreferencesKey("is_logged")
    private val TOKEN_KEY = stringPreferencesKey("token_user")
    private val EMAIL_KEY = stringPreferencesKey("email_user")
    private val NAME_KEY = stringPreferencesKey("name_user")

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                it[NAME_KEY] ?: "",
                it[TOKEN_KEY] ?: "",
                it[LOGGED_KEY] ?: false
            )
        }
    }

    suspend fun saveUSer(user: UserModel) {
        dataStore.edit {
            it[NAME_KEY] = user.name
            it[TOKEN_KEY] = user.token
            it[LOGGED_KEY] = user.isLogin
        }
    }

    suspend fun logOut() {
        dataStore.edit { it.clear() }
    }

}