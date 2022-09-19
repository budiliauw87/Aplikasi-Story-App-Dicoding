package com.liaudev.dicodingbpaa.data.local.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liaudev.dicodingbpaa.data.local.entity.RemoteEntity;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Budiliauw87 on 2022-06-15.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

@Dao
public interface RemoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RemoteEntity remoteEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RemoteEntity> remoteEntities);

    @Query("SELECT * FROM remote_keys WHERE storyId = :id")
    Single<RemoteEntity> getRemoteKeyById(String id);

    @Query("DELETE FROM remote_keys")
    void deleteRemote();
}
