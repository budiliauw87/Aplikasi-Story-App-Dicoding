package com.liaudev.dicodingbpaa.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;

/**
 * Created by Budiliauw87 on 2022-06-20.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavoriteEntity favorite);

    @Query("SELECT * FROM favorite WHERE storyId =:id")
    LiveData<FavoriteEntity> getFavorite(String id);

    @Query("DELETE FROM favorite WHERE storyId =:id")
    void deleteFavorite( String id);
}
