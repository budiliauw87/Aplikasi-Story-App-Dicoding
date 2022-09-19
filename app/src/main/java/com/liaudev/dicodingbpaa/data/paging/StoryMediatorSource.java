package com.liaudev.dicodingbpaa.data.paging;

import androidx.annotation.NonNull;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxRemoteMediator;

import com.android.volley.VolleyError;
import com.bumptech.glide.load.HttpException;
import com.liaudev.dicodingbpaa.data.AppExecutors;
import com.liaudev.dicodingbpaa.data.local.entity.RemoteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;
import com.liaudev.dicodingbpaa.data.local.room.RemoteDao;
import com.liaudev.dicodingbpaa.data.local.room.StoryDao;
import com.liaudev.dicodingbpaa.data.local.room.StoryDatabase;
import com.liaudev.dicodingbpaa.data.remote.RemoteDataSource;
import com.liaudev.dicodingbpaa.data.remote.response.StoryResponse;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Budiliauw87 on 2022-06-15.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

@ExperimentalPagingApi
public class StoryMediatorSource extends RxRemoteMediator<Integer, StoryEntity> {

    private final RemoteDataSource remoteDataSource;
    private final AppExecutors appExecutors;
    private final StoryDatabase database;
    private final StoryDao storyDao;
    private final RemoteDao remoteDao;
    private final int INITIAL_PAGE_INDEX = 1;

    public StoryMediatorSource(RemoteDataSource remoteSource, StoryDatabase storyDatabase, AppExecutors executors) {
        this.remoteDataSource = remoteSource;
        this.database = storyDatabase;
        this.storyDao = storyDatabase.storyDao();
        this.remoteDao = storyDatabase.remoteDao();
        this.appExecutors = executors;
    }

    @NonNull
    @Override
    public Single<InitializeAction> initializeSingle() {
        return Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH);
    }


    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(@NonNull LoadType loadType, @NonNull PagingState<Integer, StoryEntity> pagingState) {

        Single<RemoteEntity> remoteKeySingle = null;
        switch (loadType) {
            case REFRESH:
                //Log.e("StoryMediatorSource","REFRESH ");
                remoteKeySingle = Single.just(new RemoteEntity(null,null, 1));
                break;
            case PREPEND:
                //Log.e("StoryMediatorSource","PREPEND ");
                return Single.just(new MediatorResult.Success(true));
            case APPEND:
                //Log.e("StoryMediatorSource","APPEND ");
                // Query remoteKeyDao for the next RemoteKey.
                StoryEntity lastItem = pagingState.lastItemOrNull();
                if (lastItem == null) {
                    return Single.just(new MediatorResult.Success(true));
                }

                remoteKeySingle = remoteDao.getRemoteKeyById(lastItem.getId());
                break;
        }

        return remoteKeySingle
                .subscribeOn(Schedulers.io())
                .flatMap((Function<RemoteEntity, Single<MediatorResult>>) remoteKey -> {
                    //Log.e("StoryMediatorSource", "remoteKeySingle "+remoteKey.getNextKey());
                    if (loadType != LoadType.REFRESH && remoteKey.getNextKey() == null) {
                        return Single.just(new MediatorResult.Success(true));
                    }

                    return remoteDataSource.getStory(remoteKey.getNextKey(), pagingState.getConfig().pageSize)
                            .map((Function<StoryResponse, MediatorResult>) response -> {
                                Integer prevKey = remoteKey.getNextKey() == 1 ? null : remoteKey.getNextKey() - 1;
                                Integer nextKey = response.getListStory().isEmpty() ? null : remoteKey.getNextKey() + 1;
                                appExecutors.diskIO().execute(() -> {
                                    database.runInTransaction(() -> {
                                        if (loadType == LoadType.REFRESH) {
                                            storyDao.deleteAll();
                                            remoteDao.deleteRemote();
                                        }
                                        for (StoryEntity entity : response.getListStory()) {
                                            storyDao.insert(entity);
                                            remoteDao.insert(new RemoteEntity(entity.getId(), prevKey, nextKey));
                                        }
                                    });
                                });
                                return new MediatorResult.Success(response.getListStory().isEmpty());
                            });
                }).onErrorResumeNext(e -> {
                    if (e instanceof IOException || e instanceof HttpException || e instanceof VolleyError) {
                        return Single.just(new MediatorResult.Error(e));
                    }
                    return Single.error(e);
                });
    }

}
