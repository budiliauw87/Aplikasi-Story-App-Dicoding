package com.liaudev.dicodingbpaa.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;

import com.liaudev.dicodingbpaa.data.local.LocalDataSource;
import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.data.local.room.StoryDatabase;
import com.liaudev.dicodingbpaa.data.paging.StoryMediatorSource;
import com.liaudev.dicodingbpaa.data.remote.RemoteDataSource;
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
public class DataRepository implements DataSource {
    private volatile static DataRepository INSTANCE = null;
    private final RemoteDataSource remoteDataSource;
    private final LocalDataSource localDataSource;
    private final AppExecutors appExecutors;
    private final StoryDatabase database;

    private DataRepository(@NonNull RemoteDataSource remoteDataSource, @NonNull LocalDataSource localDataSource, @NonNull AppExecutors executors, @NonNull StoryDatabase storyDatabase) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.appExecutors = executors;
        this.database = storyDatabase;
    }

    public static DataRepository getInstance(RemoteDataSource remoteSource, LocalDataSource localSource, AppExecutors executors, StoryDatabase storyDatabase) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(remoteSource, localSource, executors, storyDatabase);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Resource<LoginResponse>> loginAccount(String username, String password) {
        return remoteDataSource.loginAccount(username, password);
    }

    @Override
    public LiveData<Resource<BaseResponse>> registerAccount(String name, String email, String password) {
        return remoteDataSource.registerAccount(name, email, password);
    }

    @Override
    public LiveData<FavoriteEntity> getFavoriteById(String id) {
        return localDataSource.getFavoriteById(id);
    }

    @Override
    public LiveData<Resource<BaseResponse>> postStoryByGuest(String descreption, File filePhoto, double lat, double lon) {
        return remoteDataSource.postStoryByGuest(descreption, filePhoto,lat,lon);
    }

    @Override
    public LiveData<Resource<BaseResponse>> postStory(String descreption, File filePhoto, double lat, double lon) {
        return remoteDataSource.postStoryByUser(descreption, filePhoto,lat,lon);
    }

    @Override
    public LiveData<Resource<StoryResponse>> getLocationStory(int page) {
        return remoteDataSource.getStoryLocation(page);
    }


    @ExperimentalPagingApi
    @Override
    public Pager<Integer,StoryEntity> loadStory() {
        return new Pager<Integer,StoryEntity>(
                new PagingConfig(5,
                        1,
                        false,
                        15),
                null,
                new StoryMediatorSource(remoteDataSource,database,appExecutors),
                ()-> database.storyDao().getAllStories());
    }

    @Override
    public void setFavorite(FavoriteEntity favoriteEntity) {
        appExecutors.diskIO().execute(() -> {
            localDataSource.setFavorite(favoriteEntity);
        });

    }

    @Override
    public void deleteFavorite(String id) {
        appExecutors.diskIO().execute(() -> {
            localDataSource.deleteFavorite(id);
        });

    }

}
