package com.liaudev.dicodingbpaa.di;

import android.content.Context;

import com.liaudev.dicodingbpaa.data.AppExecutors;
import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.local.LocalDataSource;
import com.liaudev.dicodingbpaa.data.local.room.StoryDatabase;
import com.liaudev.dicodingbpaa.data.remote.RemoteDataSource;
import com.liaudev.dicodingbpaa.network.ApiRequest;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class Injection {
    public static DataRepository provideRepository(Context context) {
        AppExecutors appExecutors = new AppExecutors();
        StoryDatabase storyDatabase = StoryDatabase.getInstance(context);
        LocalDataSource localDataSource = LocalDataSource.getInstance(storyDatabase);
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance(new ApiRequest(context));
        return DataRepository.getInstance(remoteDataSource, localDataSource,appExecutors,storyDatabase);
    }
}
