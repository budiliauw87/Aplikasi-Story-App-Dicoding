package com.liaudev.dicodingbpaa.data.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.RemoteEntity;
import com.liaudev.dicodingbpaa.data.local.entity.StoryEntity;

/**
 * Created by Budiliauw87 on 2022-05-17.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
@Database(entities = {StoryEntity.class, RemoteEntity.class, FavoriteEntity.class},version = 1,exportSchema = false)
public abstract class StoryDatabase extends RoomDatabase {
    private static volatile StoryDatabase INSTANCE;

    //construct class
    public abstract StoryDao storyDao();
    public abstract RemoteDao remoteDao();
    public abstract FavoriteDao favoriteDao();
    public static StoryDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (StoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StoryDatabase.class, "storybpaa.db").build();
                }
            }
        }
        return INSTANCE;
    }
}