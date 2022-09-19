package com.liaudev.dicodingbpaa.data.remote.response;


import java.util.Map;

/**
 * Created by Budiliauw87 on 2022-05-26.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class LoginResponse extends BaseResponse {

    private Map<String, String> loginResult;

    public LoginResponse() {}

    public Map<String, String> getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(Map<String, String> loginResult) {
        this.loginResult = loginResult;
    }

}
