package com.liaudev.dicodingbpaa.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;

import java.util.List;

/**
 * Created by Budiliauw87 on 2022-05-17.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
@Dao
public interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<StoryEntity> storyEntities);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StoryEntity storyEntity);

    @Query("DELETE FROM story")
    void deleteAll();

    @Query("DELETE FROM story WHERE id=:id")
    void delete(String id);

    @Query("SELECT * from story")
    PagingSource<Integer, StoryEntity> getAllStories();

    @Query("SELECT * FROM story WHERE id = :id")
    LiveData<StoryEntity> getStory(String id);
}
