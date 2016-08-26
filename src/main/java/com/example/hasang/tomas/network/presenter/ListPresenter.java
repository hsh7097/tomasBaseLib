package com.example.hasang.tomas.network.presenter;

import com.example.hasang.tomas.network.Model;

import java.util.ArrayList;

/**
 * Created by hasang on 16. 1. 25..
 */
public interface ListPresenter extends Presenter {
    void onSuccess(ArrayList<Model> arrayData);
}
