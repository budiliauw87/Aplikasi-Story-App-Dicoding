package com.liaudev.dicodingbpaa.data.local;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;

import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.data.local.room.StoryDatabase;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

public class LocalDataSource {
    private final StoryDatabase storyDatabase;
    private static LocalDataSource instance;
    private LocalDataSource(StoryDatabase database) {
        this.storyDatabase = database;
    }

    public static LocalDataSource getInstance(StoryDatabase storyDatabase) {
        if (instance == null) {
            instance = new LocalDataSource(storyDatabase);
        }
        return instance;
    }

    public PagingSource<Integer, StoryEntity> getFavorite() {
        return storyDatabase.storyDao().getAllStories();
    }

    public void setFavorite(FavoriteEntity favorite) {
        Log.e("LocalDataSource","Insert favorite");
        storyDatabase.favoriteDao().insert(favorite);
    }
    public LiveData<FavoriteEntity> getFavoriteById(String id){
       return storyDatabase.favoriteDao().getFavorite(id);
    }
    public void deleteFavorite(String id) {
        storyDatabase.favoriteDao().deleteFavorite(id);
    }

}
