package com.example.hasang.tomas;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by hasang on 16. 1. 21..
 */
public class AppPreferenceManager {
    private static final String PREF_NAME1 = "com.unionpool.a.dottegi.pref";

    private final String PREF_NAME = "AppPreference";
    public static final String STRING_DEFAULT = "";
    public static final int INT_DEFAULT = 0;
    public static final float FLOAT_DEFAULT = 0;
    public static final boolean BOOLEAN_DEFAULT = false;

    private static AppPreferenceManager mInstance = null;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private AppPreferenceManager() {
        mPrefs = AppContext.getInstance().getApplicationContext().getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public static AppPreferenceManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppPreferenceManager();
        }
        return mInstance;
    }

    public void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void put(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void put(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void put(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void put(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String get(String key, String emptyValue) {
        return mPrefs.getString(key, emptyValue);
    }

    public int get(String key, int emptyValue) {
        return mPrefs.getInt(key, emptyValue);
    }

    public long get(String key, long emptyValue) {
        return mPrefs.getLong(key, emptyValue);
    }

    public float get(String key, float emptyValue) {
        return mPrefs.getFloat(key, emptyValue);
    }

    public boolean get(String key, boolean emptyValue) {
        return mPrefs.getBoolean(key, emptyValue);
    }

    public String getString(String key) {
        return mPrefs.getString(key, STRING_DEFAULT);
    }

    public int getInt(String key) {
        return mPrefs.getInt(key, INT_DEFAULT);
    }

    public float getFloat(String key) {
        return mPrefs.getFloat(key, FLOAT_DEFAULT);
    }

    public boolean getBoolean(String key) {
        return mPrefs.getBoolean(key, BOOLEAN_DEFAULT);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }

    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }
}
