package com.example.hasang.tomas.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasang.tomas.AppContext;

/**
 * Created by hasang on 16. 1. 25..
 */
public abstract class TomasFragment extends Fragment {

    private static final String TAG = TomasFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        AppContext.getInstance().hideKeyboard();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    /**
     * 데이터를 업데이트 하기위한 추상메소드
     *
     * @param args 다른 액티비티 또는 프래그먼트에서 가져오는 업데이트 될 데이터
     */
    public abstract void updateData(Bundle args);
}
