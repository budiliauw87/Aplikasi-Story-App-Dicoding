package com.liaudev.dicodingbpaa.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Budiliauw87 on 2022-05-23.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class ApiRequest {

    private static final String TAG = ApiRequest.class.getSimpleName();
    // DEFAULT 10 SECOND FOR TIMEOUT
    private final int INITIAL_TIME_OUT = 10000,MIDDLE_TIME_OUT = 30000,MAXIMUM_TIME_OUT = 60000;

    private int MODE_TIMEOUT = 0,MAX_NUM_RETRY = 0;
    private RequestQueue requestQueue;
    private final Context context;

    public ApiRequest(Context context) {
        this.context = context;
    }
    public void setMODE_TIMEOUT (int mode_timeout){
        this.MODE_TIMEOUT = mode_timeout;
    }
    public void setMAX_NUM_RETRY (int MAX_NUM){
        this.MAX_NUM_RETRY = MAX_NUM;
    }


    private RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /* Simple Request Queue */
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        if(MODE_TIMEOUT == 1){
            request.setRetryPolicy(new DefaultRetryPolicy(MIDDLE_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else if(MODE_TIMEOUT == 2){
            request.setRetryPolicy(new DefaultRetryPolicy(MAXIMUM_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else{
            request.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        getRequestQueue().add(request);
    }

    /**
     * Request queue using tag
     */
    private <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        if(MODE_TIMEOUT == 1){
            request.setRetryPolicy(new DefaultRetryPolicy(MIDDLE_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else if(MODE_TIMEOUT == 2){
            request.setRetryPolicy(new DefaultRetryPolicy(MAXIMUM_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else{
            request.setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIME_OUT, MAX_NUM_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll((req) -> { return true; });
        }
    }
    public void clearCacheRequest(){
        RequestQueue requestQueue = getRequestQueue();
        requestQueue.getCache().clear();
    }

    public boolean isNetworkAvailable(){
        return Utils.isNetworkAvailable(context);
    }

    /**
     * Parse error text
     * @return String error message
     */
    public String parseNetworkError(VolleyError volleyError){
        NetworkResponse networkResponse = volleyError.networkResponse;
        String msgError = context.getString(R.string.something_error);
        if (networkResponse != null) {
            String jsonError = new String(networkResponse.data);
            try {
                JSONObject obj = new JSONObject(jsonError);
                msgError = obj.getString("message");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return msgError;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        if(App.getInstance().isLogin() && !App.getInstance().getToken().trim().isEmpty()){
            params.put("Authorization", String.format(context.getString(R.string.token_format),App.getInstance().getToken()));
        }

        return params;
    }
}
