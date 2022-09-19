package com.liaudev.dicodingbpaa.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.liaudev.dicodingbpaa.data.DataRepository;
import com.liaudev.dicodingbpaa.data.remote.response.LoginResponse;
import com.liaudev.dicodingbpaa.vo.Resource;

/**
 * Created by Budiliauw87 on 2022-05-25.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class LoginViewModel extends ViewModel {
    private final DataRepository dataRepository;
    public LoginViewModel(DataRepository repository) {
        this.dataRepository = repository;
    }


    public LiveData<Resource<LoginResponse>> loginAccount(String email, String password){
        return dataRepository.loginAccount(email,password);
    }

}
