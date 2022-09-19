package com.liaudev.dicodingbpaa.data.remote.response;

/**
 * Created by Budiliauw87 on 2022-05-29.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class BaseResponse {

    private String message;
    private boolean error;

    public BaseResponse(){}

    public BaseResponse(boolean err, String msg) {
        this.error = err;
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
