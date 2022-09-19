package com.liaudev.dicodingbpaa.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.di.Injection;
import com.liaudev.dicodingbpaa.ui.detail.DetailViewModel;
import com.liaudev.dicodingbpaa.ui.home.HomeViewModel;
import com.liaudev.dicodingbpaa.ui.login.LoginViewModel;
import com.liaudev.dicodingbpaa.ui.map.MapViewModel;
import com.liaudev.dicodingbpaa.ui.post.PostViewModel;
import com.liaudev.dicodingbpaa.ui.register.RegisterViewModel;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final DataRepository dataRepository;

    private ViewModelFactory(DataRepository repository) {
        dataRepository = repository;
    }

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(Injection.provideRepository(context));
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(dataRepository);
        }else if(modelClass.isAssignableFrom(HomeViewModel.class)){
            return (T) new HomeViewModel(dataRepository);
        }else if(modelClass.isAssignableFrom(MapViewModel.class)){
            return (T) new MapViewModel(dataRepository);
        }else if(modelClass.isAssignableFrom(RegisterViewModel.class)){
            return (T) new RegisterViewModel(dataRepository);
        }else if(modelClass.isAssignableFrom(DetailViewModel.class)){
            return (T) new DetailViewModel(dataRepository);
        }else if(modelClass.isAssignableFrom(PostViewModel.class)){
            return (T) new PostViewModel(dataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
