package com.example.hasang.tomas.network.retrofit;

import android.util.Log;

import rx.Subscriber;


/**
 * Created by hasang on 16. 8. 26..
 * 공통된 콜백 처리를 위해 입력 된 Rx 콜백 생성기
 */
public abstract class TomasRetrofitRxCallBack<T> extends Subscriber<T> {
    private final String TAG = "RetrofitRxCallBack";

    @Override
    public void onNext(T rpUsers) {
        Log.e(TAG, "onNext : " + rpUsers.toString());
        handleResponse(rpUsers);
    }

    @Override
    public void onCompleted() {
        Log.e(TAG, "onCompleted");
        onSuccess();
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }


    public abstract void handleResponse(T result);
    public abstract void onSuccess();
    public abstract void onFailure(Throwable t);

}