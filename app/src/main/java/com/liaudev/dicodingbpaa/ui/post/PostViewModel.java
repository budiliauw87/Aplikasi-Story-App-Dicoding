package com.liaudev.dicodingbpaa.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.remote.response.BaseResponse;
import com.liaudev.dicodingbpaa.vo.Resource;

import java.io.File;

/**
 * Created by Budiliauw87 on 2022-06-10.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class PostViewModel extends ViewModel {
    private final DataRepository dataRepository;

    public PostViewModel(DataRepository repository) {
        this.dataRepository = repository;
    }

    public LiveData<Resource<BaseResponse>> postStory(String descreption, File photoFile, double lat, double lon, boolean isPostByGuest) {
        if (isPostByGuest) {
            return dataRepository.postStoryByGuest(descreption, photoFile,lat,lon);
        } else {
            return dataRepository.postStory(descreption, photoFile,lat,lon);
        }

    }
}
