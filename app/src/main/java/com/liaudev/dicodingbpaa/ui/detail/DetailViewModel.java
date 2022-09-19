package com.liaudev.dicodingbpaa.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.local.entity.FavoriteEntity;

/**
 * Created by Budiliauw87 on 2022-06-09.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class DetailViewModel extends ViewModel {
    private final DataRepository dataRepository;

    public DetailViewModel(DataRepository repository) {
        dataRepository = repository;
    }

    void setFavorite(FavoriteEntity favorite) {
        dataRepository.setFavorite(favorite);
    }

    void deleteFavorite(String id) {
        dataRepository.deleteFavorite(id);
    }

    public LiveData<FavoriteEntity> getFavorite(String id) {
        return dataRepository.getFavoriteById(id);
    }

}
