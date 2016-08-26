package com.example.hasang.tomas.network.retrofit;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hasang on 16. 8. 23..
 * 공통된 콜백 처리를 위해 입력 된 콜백 생성기
 */
public abstract class TomasRetrofitCallBack<T> implements Callback<T> {
    private final String TAG = "Retrofit";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            handleResponse(response.body());
        } else {
            Log.e(TAG,"errorCode : " + response.code());
            Log.e(TAG,"errorBody : " + response.errorBody().contentType().toString());
            try {
                Log.e(TAG,"errorBody bytes : " + response.errorBody().bytes().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Log.e(TAG,"errorBody string : " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                APIError error = new Gson().fromJson(response.errorBody().string(), APIError.class);
//                handleOnFailed(error.error);
//            } catch (NullPointerException | JsonSyntaxException nullException) {
//                try {
//                    LogUtil.LogE(response.errorBody().string());
//                } catch (IOException e) {
//                    onFailure(e);
//                    e.printStackTrace();
//                }
//            } catch (IOException e) {
//                onFailure(e);
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t);
    }

    public abstract void handleResponse(T result);


    public abstract void onFailure(Throwable t);

}
