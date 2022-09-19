package com.liaudev.dicodingbpaa.ui.home;


import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;

import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends ViewModel {
    private final DataRepository dataRepository;
    @SuppressLint("UnsafeOptInUsageError")
    public HomeViewModel(DataRepository repository) {
        this.dataRepository = repository;

    }
    @ExperimentalPagingApi
    public LiveData<PagingData<StoryEntity>> getStories() {
         CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
         return PagingLiveData.cachedIn(PagingLiveData.getLiveData(dataRepository.loadStory()), coroutineScope);

    }
}