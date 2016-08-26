package com.example.hasang.tomas.network.retrofit.interceptor;

import android.util.Log;

import com.example.hasang.tomas.network.retrofit.TomasRetrofitToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hasang on 16. 8. 23..
 */
public class TomasRetrofitTokenInterceptor implements Interceptor {

    private static final String TAG = "TomasInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header(TomasRetrofitToken.API_REQUEST_TOKEN, TomasRetrofitToken.getToken())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();

        Response response = chain.proceed(request);

        if (response.isSuccessful()) {
            if (response.headers().get(TomasRetrofitToken.API_REQUEST_TOKEN) != null) {
                Log.e(TAG,"token : " + response.headers().get(TomasRetrofitToken.API_REQUEST_TOKEN));
                TomasRetrofitToken.setToken(response.headers().get(TomasRetrofitToken.API_REQUEST_TOKEN));
            }
        }


        return response;
    }

}
