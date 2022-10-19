package com.liaudev.storydicoding.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mysubmission_intermediate.Model.UserModel
import com.liaudev.storydicoding.data.StoryRepository


/**
 * Created by Budiliauw87 on 2022-10-19.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
class SplashViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getUser():LiveData<UserModel> = storyRepository.getUser().asLiveData()
}
