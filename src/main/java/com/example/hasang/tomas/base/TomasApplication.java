package com.example.hasang.tomas.base;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.hasang.tomas.AppContext;

import java.util.ArrayList;

/**
 * Created by hasang on 16. 8. 1..
 */
public class TomasApplication extends MultiDexApplication implements ActivityLifecycleCallbacks {
    public static final String TAG = TomasApplication.class.getName();

    private static boolean isRunning = false;

    private ArrayList<AppCompatActivity> activitys;

    private static TomasApplication mInstance;

    public static TomasApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        activitys = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppContext.getInstance().setApplicationContext(getApplicationContext());
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isRunning = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isRunning = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 모든 액티비티 삭제
     */
    public void clearActivity() {
        if (activitys == null) return;
        for (AppCompatActivity activity : activitys) {
            activity.finish();
        }
        activitys.clear();
    }

    /**
     * 해당 액티비티 제외 삭제
     */
    public void removeActivity() {
        for (AppCompatActivity activity : activitys) {
            Log.d(TAG, "저장된 Activity : " + activity);
        }
    }

    /**
     * 액티비티 추가
     *
     * @param activity 추가될 액티비티
     */
    public void addActivity(AppCompatActivity activity) {
        activitys.add(activity);
    }

    /**
     * 액티비티 로그 출력
     */
    public void logActivitys() {
        Log.d(TAG, "-------------------------------------");
        for (AppCompatActivity activity : activitys) {
            Log.d(TAG, "저장된 Activity : " + activity);
        }
        Log.d(TAG, "-------------------------------------");
    }

    /**
     * 어플리케이션이 현재 실행중인지 체크
     *
     * @return 어플리케이션의 상태
     */
    public boolean isApplicationRunning() {
        return isRunning;
    }
}
