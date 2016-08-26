package com.example.hasang.tomas.network.presenter;

import com.example.hasang.tomas.network.Model;


/**
 * Created by hasang on 16. 1. 25..
 */
public interface Presenter {
    void onSuccess(Model response);

    void onNullData();

    void onFailed(String errorMessage);
}
