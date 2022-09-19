package com.liaudev.dicodingbpaa.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Budiliauw87 on 2022-06-20.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
@Entity(tableName = "favorite")
public class FavoriteEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "storyId")
    public String storyId;

    public FavoriteEntity(@NonNull String storyId) {
        this.storyId = storyId;
    }

    @NonNull
    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(@NonNull String storyId) {
        this.storyId = storyId;
    }

}
