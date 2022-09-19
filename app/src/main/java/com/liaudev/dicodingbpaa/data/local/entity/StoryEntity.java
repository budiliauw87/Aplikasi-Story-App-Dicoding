package com.liaudev.dicodingbpaa.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Budiliauw87 on 2022-05-26.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

@Entity(tableName = "story")
public class StoryEntity implements Parcelable {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "photoUrl")
    private String photoUrl;

    @ColumnInfo(name = "createdAt")
    private String createdAt;

    @ColumnInfo(name = "lat",defaultValue = "0.0")
    private Double lat;

    @ColumnInfo(name = "lon",defaultValue = "0.0")
    private Double lon;

    public StoryEntity(){}

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(photoUrl);
        parcel.writeString(createdAt);
        parcel.writeByte(lat == null?(byte) 0:(byte) 1);
        parcel.writeByte(lat == null?(byte) 0:(byte) 1);
    }

    protected StoryEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        photoUrl = in.readString();
        createdAt = in.readString();
        lat = in.readByte() == 0 ?null: in.readDouble();
        lon = in.readByte() == 0 ?null: in.readDouble();
    }

    public static final Creator<StoryEntity> CREATOR = new Creator<StoryEntity>() {
        @Override
        public StoryEntity createFromParcel(Parcel in) {
            return new StoryEntity(in);
        }

        @Override
        public StoryEntity[] newArray(int size) {
            return new StoryEntity[size];
        }
    };
}
