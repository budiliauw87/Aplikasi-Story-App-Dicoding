package com.liaudev.dicodingbpaa.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.remote.response.StoryResponse;
import com.liaudev.dicodingbpaa.vo.Resource;

public class MapViewModel extends ViewModel {
    private final DataRepository dataRepository;

    public MapViewModel(DataRepository repository) {
        dataRepository = repository;
    }

    public LiveData<Resource<StoryResponse>> getStoryLocation(int page) {
        return dataRepository.getLocationStory(page);
    }
}