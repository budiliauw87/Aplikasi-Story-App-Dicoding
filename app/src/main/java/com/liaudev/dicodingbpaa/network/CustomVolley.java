package com.liaudev.dicodingbpaa.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.liaudev.dicodingbpaa.App;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Budiliauw87 on 2022-06-04.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class CustomVolley<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Type type;
    //private final Class<T> clazz;
    private final Response.Listener<T> listener;
    private final Map<String,String> params;

    public CustomVolley(int method,String url, Map<String, String> params,Type type,
                         Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        //this.clazz = clazz;
        this.listener = reponseListener;
        this.params = params;
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(jsonString,type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        if(App.getInstance().isLogin() && !App.getInstance().getToken().trim().isEmpty()){
            params.put("Authorization", "Bearer "+App.getInstance().getToken());
        }

        return params;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
