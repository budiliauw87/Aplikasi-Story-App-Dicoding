package com.liaudev.dicodingbpaa.data;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;

import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.data.remote.response.BaseResponse;
import com.liaudev.dicodingbpaa.data.remote.response.LoginResponse;
import com.liaudev.dicodingbpaa.data.remote.response.StoryResponse;
import com.liaudev.dicodingbpaa.vo.Resource;

import java.io.File;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public interface DataSource {
    LiveData<Resource<LoginResponse>> loginAccount(String username, String password);
    LiveData<Resource<BaseResponse>> registerAccount(String email, String name, String password);
    LiveData<FavoriteEntity> getFavoriteById(String id);
    LiveData<Resource<BaseResponse>> postStoryByGuest(String descreption, File filePhoto, double lat, double lon);
    LiveData<Resource<BaseResponse>> postStory(String descreption, File filePhoto, double lat, double lon);
    LiveData<Resource<StoryResponse>> getLocationStory(int page);
    Pager<Integer,StoryEntity> loadStory();
    void setFavorite(FavoriteEntity favorite);
    void deleteFavorite(String id);
}
