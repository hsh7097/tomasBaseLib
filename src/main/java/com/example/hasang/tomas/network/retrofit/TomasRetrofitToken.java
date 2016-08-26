package com.example.hasang.tomas.network.retrofit;

import com.example.hasang.tomas.AppPreferenceManager;

/**
 * Created by hasang on 16. 8. 23..
 */
public class TomasRetrofitToken {
    public static final String API_REQUEST_TOKEN = "X-AUTH-TOKEN";


    private static String token;

    public static String getToken() {
        if (token == null || token.isEmpty()) {
            token = AppPreferenceManager.getInstance().get(API_REQUEST_TOKEN, "");
        }
        return token;
    }

    public static void setToken(String setToken) {
        if (setToken != null && token == setToken) {
            return;
        }

        token = setToken;
        AppPreferenceManager.getInstance().put(API_REQUEST_TOKEN, token);
    }
}
