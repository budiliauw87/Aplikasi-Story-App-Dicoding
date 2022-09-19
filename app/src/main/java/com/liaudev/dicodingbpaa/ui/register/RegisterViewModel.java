package com.liaudev.dicodingbpaa.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.remote.response.BaseResponse;
import com.liaudev.dicodingbpaa.vo.Resource;

/**
 * Created by Budiliauw87 on 2022-05-30.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class RegisterViewModel extends ViewModel {
    private final DataRepository dataRepository;

    public RegisterViewModel(DataRepository repository) {
        this.dataRepository = repository;
    }

    public LiveData<Resource<BaseResponse>> registerAccount(String username, String email, String password){
        return dataRepository.registerAccount(username,email,password);
    }

}
