package com.liaudev.dicodingbpaa.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Budiliauw87 on 2022-06-15.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
@Entity(tableName = "remote_keys")
public class RemoteEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "storyId")
    public String id;
    @ColumnInfo(name = "prevKey")
    public Integer prevKey;
    @ColumnInfo(name = "nextKey")
    public Integer nextKey;

    public RemoteEntity(@NonNull String id, Integer prevKey, Integer nextKey) {
        this.id = id;
        this.prevKey = prevKey;
        this.nextKey = nextKey;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Integer getPrevKey() {
        return prevKey;
    }

    public void setPrevKey(Integer prevKey) {
        this.prevKey = prevKey;
    }

    public Integer getNextKey() {
        return nextKey;
    }

    public void setNextKey(Integer nextKey) {
        this.nextKey = nextKey;
    }

}
