package com.liaudev.dicodingbpaa.data.remote;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.Constants;
import com.liaudev.dicodingbpaa.data.remote.response.BaseResponse;
import com.liaudev.dicodingbpaa.data.remote.response.LoginResponse;
import com.liaudev.dicodingbpaa.data.remote.response.StoryResponse;
import com.liaudev.dicodingbpaa.network.ApiRequest;
import com.liaudev.dicodingbpaa.network.CustomMultiPartRequest;
import com.liaudev.dicodingbpaa.network.CustomVolley;
import com.liaudev.dicodingbpaa.utils.Utils;
import com.liaudev.dicodingbpaa.vo.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;


/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class RemoteDataSource extends Constants {
    private static RemoteDataSource INSTANCE;
    private final ApiRequest apiRequest;

    private RemoteDataSource(ApiRequest _apiRequest) {
        this.apiRequest = _apiRequest;
    }

    public static RemoteDataSource getInstance(ApiRequest _apiRequest) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(_apiRequest);
        }
        return INSTANCE;
    }

    /**
     * @return livedata object
     */
    public LiveData<Resource<LoginResponse>> loginAccount(String email, String password) {
        MutableLiveData<Resource<LoginResponse>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Resource.loading(null));
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("email", email);
            jsonparams.put("password", password);
            final String payload = jsonparams.toString();
            final Type type = new TypeToken<LoginResponse>() {
            }.getType();
            apiRequest.addToRequestQueue(new CustomVolley<LoginResponse>(Request.Method.POST, LOGIN_URL, null, type, response -> {
                mutableLiveData.setValue(Resource.success(response));
            }, error -> {
                final String errorMsg = apiRequest.parseNetworkError(error);
                mutableLiveData.setValue(Resource.error(errorMsg, null));
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return apiRequest.getHeaders();
                }

                @Override
                public byte[] getBody() {
                    return payload == null ? null : payload.getBytes(StandardCharsets.UTF_8);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            mutableLiveData.setValue(Resource.error("Something Error", null));
        }
        return mutableLiveData;
    }

    public LiveData<Resource<BaseResponse>> registerAccount(String name, String email, String password) {
        MutableLiveData<Resource<BaseResponse>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Resource.loading(null));
        try {
            JSONObject jsonparams = new JSONObject();
            jsonparams.put("name", name);
            jsonparams.put("email", email);
            jsonparams.put("password", password);
            final String payload = jsonparams.toString();
            final Type type = new TypeToken<BaseResponse>() {
            }.getType();

            apiRequest.addToRequestQueue(new CustomVolley<BaseResponse>(Request.Method.POST, REGISTER_URL,
                    null, type, response -> {
                mutableLiveData.setValue(Resource.success(response));
            }, error -> {
                final String errorMsg = apiRequest.parseNetworkError(error);
                mutableLiveData.setValue(Resource.error(errorMsg, null));
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return apiRequest.getHeaders();
                }

                @Override
                public byte[] getBody() {
                    return payload == null ? null : payload.getBytes(StandardCharsets.UTF_8);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            mutableLiveData.setValue(Resource.error("Something Error", null));
        }
        return mutableLiveData;
    }


    public LiveData<Resource<BaseResponse>> postStoryByGuest(String descreption, File filePhoto, double lat, double lon) {
        MutableLiveData<Resource<BaseResponse>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Resource.loading(null));
        final byte[] bytesFile = Utils.getFileData(filePhoto);
        if (bytesFile != null) {
            CustomMultiPartRequest customMultiPartRequest = new CustomMultiPartRequest(Request.Method.POST, ADD_BY_GUEST, null, (response) -> {
                BaseResponse baseResponse = new BaseResponse();
                try {
                    baseResponse.setError(response.getBoolean("error"));
                    baseResponse.setMessage(response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mutableLiveData.setValue(Resource.success(baseResponse));
                }

            }, (error) -> {
                final String errorMsg = apiRequest.parseNetworkError(error);
                mutableLiveData.setValue(Resource.error(errorMsg, null));
            });
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("description", descreption));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("lat", String.valueOf(lat)));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("lon", String.valueOf(lon)));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FilePart("photo", "image/jpeg", filePhoto.getName(), bytesFile));
            apiRequest.addToRequestQueue(customMultiPartRequest);
        } else {
            mutableLiveData.setValue(Resource.error("Photo Byte error", null));
        }

        return mutableLiveData;
    }

    public LiveData<Resource<BaseResponse>> postStoryByUser(String descreption, File filePhoto, double lat, double lon) {
        MutableLiveData<Resource<BaseResponse>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Resource.loading(null));
        final byte[] bytesFile = Utils.getFileData(filePhoto);
        if (bytesFile != null) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer " + App.getInstance().getToken());
            CustomMultiPartRequest customMultiPartRequest = new CustomMultiPartRequest(Request.Method.POST, ADD_STORIES, headers, (response) -> {
                mutableLiveData.setValue(Resource.success(null));
            }, (error) -> {
                final String errorMsg = apiRequest.parseNetworkError(error);
                mutableLiveData.setValue(Resource.error(errorMsg, null));
            });
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("lat", String.valueOf(lat)));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("lon", String.valueOf(lon)));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FormPart("description", descreption));
            customMultiPartRequest.addPart(new CustomMultiPartRequest.FilePart("photo", "image/jpeg", filePhoto.getName(), bytesFile));
            apiRequest.addToRequestQueue(customMultiPartRequest);
        } else {
            mutableLiveData.setValue(Resource.error("Photo Byte error", null));
        }

        return mutableLiveData;
    }

    public Single<StoryResponse> getStory(int pageNum, int pageSize) {
        return Single.create((storyResponse) -> {
            try {
                Type type = new TypeToken<StoryResponse>() {
                }.getType();
                final String url = GET_STORIES + pageNum + "&size=" + pageSize;
                apiRequest.addToRequestQueue(new CustomVolley<StoryResponse>(Request.Method.GET, url, null, type, response -> {
                    storyResponse.onSuccess(response);
                }, error -> {
                    final String errorMsg = apiRequest.parseNetworkError(error);
                    storyResponse.onError(new VolleyError(errorMsg));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return apiRequest.getHeaders();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                storyResponse.onError(e);
            }
        });
    }

    public LiveData<Resource<StoryResponse>> getStoryLocation(int pageNum) {
        MutableLiveData<Resource<StoryResponse>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(Resource.loading(null));
        Type type = new TypeToken<StoryResponse>() {
        }.getType();
        final String url = GET_STORIES_LOCATION + pageNum;
        apiRequest.addToRequestQueue(new CustomVolley<StoryResponse>(Request.Method.GET, url, null, type, response -> {
            mutableLiveData.setValue(Resource.success(response));
        }, error -> {
            final String errorMsg = apiRequest.parseNetworkError(error);
            mutableLiveData.setValue(Resource.error(errorMsg, null));
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return apiRequest.getHeaders();
            }
        });
        return mutableLiveData;
    }
}
