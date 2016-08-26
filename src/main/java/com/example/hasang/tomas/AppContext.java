package com.example.hasang.tomas;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.hasang.tomas.base.TomasFragment;

import java.util.ArrayList;

/**
 * Created by hasang on 16. 1. 21..
 */
public class AppContext {
    public static final int ORIENTATION_NONE = -1;
    public static final int ORIENTATION_PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    public static final int SIZE_ZERO = 0;

    private static final String PORTRAIT_WIDTH = "portrait_widht";
    private static final String PORTRAIT_HEIGHT = "portrait_height";
    private static final String LANDSCAPE_WIDTH = "landscape_widht";
    private static final String LANDSCAPE_HEIGHT = "landscape_height";

    private static AppContext mInstance;
    private static AppCompatActivity mActivity;
    private static Context mApplicationContext;
    private static Context mContext;

    private Rect mPortraitScreenRect;
    private Rect mLandScapeScreenRect;

    public enum FragmentTransitionType {
        Default,
        FadeInOut,
        SlideLeft,
        SlideRight
    }

    private AppContext() {

    }

    public static synchronized AppContext getInstance() {
        if (mInstance == null) {
            mInstance = new AppContext();
        }
        return mInstance;
    }

    public void setActivity(AppCompatActivity activity) {
        if (activity == null) return;
        mActivity = activity;
        mContext = activity.getBaseContext();
    }

    public void setApplicationContext(Context context) {
        if (context == null) return;
        mApplicationContext = context;
    }

    public Context getApplicationContext() {
        return mApplicationContext;
    }

    public Context getContext() {
        return mContext;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    /**
     * 키보드 숨김
     */
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showKeyboard(View view){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * @param resId 요청 ID
     * @return 해당 스트링 값
     */
    public String getString(int resId) {
        Resources rsc = getResources();
        if (rsc == null) return null;
        return rsc.getString(resId);
    }

    /**
     * @param dimensionId 요청 ID
     * @return 해당 dimension 값
     */
    public float getDemension(int dimensionId) {
        Resources rsc = getResources();
        if (rsc == null) return SIZE_ZERO;
        return rsc.getDimension(dimensionId);
    }

    /**
     * @param resId 요청 ID
     * @return 해당 스트링 배열
     */
    public String[] getStringArray(int resId) {
        Resources rsc = getResources();
        if (rsc == null) return null;
        return rsc.getStringArray(resId);
    }

    /**
     * @param resId 요청 ID
     * @return 해당 스트링 배열
     */
    public TypedArray getTypedArray(int resId) {
        Resources rsc = getResources();
        if (rsc == null) return null;
        return rsc.obtainTypedArray(resId);
    }

    /**
     * @param colorId 요청 ID
     * @return 해당 color
     */
    public int getColor(int colorId) {
        Resources rsc = getResources();
        if (rsc == null) return SIZE_ZERO;
        return rsc.getColor(colorId);
    }

    /**
     * @param drawableId 요청 ID
     * @return 해당 drawable
     */
    public Drawable getDrawable(int drawableId) {
        Resources rsc = getResources();
        if (rsc == null) return null;
        return rsc.getDrawable(drawableId);
    }

    /**
     * 화면 회전상태 요청
     *
     * @return 화면 회전에 따라 값 호출
     */
    public int getOrientation() {
        Resources rsc = getResources();
        if (rsc == null) return ORIENTATION_NONE;
        if (rsc.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return ORIENTATION_PORTRAIT;
        } else {
            return ORIENTATION_LANDSCAPE;
        }
    }

    public TelephonyManager getTelephoneManager() {
        return (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String getTelephoneNumber() {
        return getTelephoneManager().getLine1Number();

    }

    /**
     * @return 현재 회전된 화면의 사이즈
     */
    public Rect getScreenRect() {
        if (getOrientation() == ORIENTATION_PORTRAIT) {
            return getPortraitRect();
        } else {
            return getLandscapeRect();
        }
    }

    /**
     * @param orientationValue 요청하는 회전값
     * @return 요청된 값에 따라 화면 사이즈 값 호출
     */
    public Rect getScreenRect(int orientationValue) {

        if (orientationValue == ORIENTATION_PORTRAIT) {
            return getPortraitRect();
        } else {
            return getLandscapeRect();
        }
    }


    /**
     * 벳지 카운트 수정
     * @param bedgeCount 수정될 벳지 카운트
     */
    public void setBedgeCount(int bedgeCount){
        if(getActivity() == null) return;
        if(getActivity().getComponentName() == null) return;
        if(getActivity().getComponentName().getPackageName() == null) return;
        if(getActivity().getComponentName().getClassName() == null) return;

        ComponentName componentName = getActivity().getComponentName();


        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("bedge_count_package_name",componentName.getPackageName());
        intent.putExtra("bedge_count_class_name",componentName.getClassName());
        intent.putExtra("badge_count",bedgeCount);
        getActivity().sendBroadcast(intent);

    }

    public DisplayMetrics getDisplayMatrics() {
        if (getResources() == null) return null;
        return getResources().getDisplayMetrics();
    }

    public FragmentManager getSupportFragmentManager() {
        if (mActivity == null) return null;
        return mActivity.getSupportFragmentManager();

    }

    public FragmentTransaction getFragmentTransaction() {
        if (getSupportFragmentManager() == null) return null;
        return getSupportFragmentManager().beginTransaction();
    }

    public void pushFragment(Bundle args, TomasFragment pushFragment, int parentResId, String pushFragmentTag, boolean addType, boolean backStackType, FragmentTransitionType transitionType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (AppContext.getInstance().getActivity().isDestroyed()) return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(pushFragmentTag);

        if (fragment != null && fragment instanceof TomasFragment) {
            pushFragment.updateData(args);
            return;
        }

        pushFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        if (transitionType != FragmentTransitionType.Default) {
            if (transitionType == FragmentTransitionType.FadeInOut) {
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            } else if (transitionType == FragmentTransitionType.SlideLeft) {
//                fragmentTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right);
                fragmentTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_in_right);
            } else if (transitionType == FragmentTransitionType.SlideRight) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            }
        }

        if (addType) {
            fragmentTransaction.add(parentResId, pushFragment, pushFragmentTag);
        } else {
            fragmentTransaction.replace(parentResId, pushFragment);
        }
        if (backStackType) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void pushChildFragment(Bundle args, TomasFragment parentFragment, TomasFragment pushFragment, int parentResId, String pushFragmentTag, boolean addType, boolean backStackType, FragmentTransitionType transitionType) {
        Fragment fragment = parentFragment.getChildFragmentManager().findFragmentByTag(pushFragmentTag);

        if (fragment != null && fragment instanceof TomasFragment) {
            pushFragment.updateData(args);
            return;
        }

        pushFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentTransaction();

        if (transitionType != FragmentTransitionType.Default) {
            if (transitionType == FragmentTransitionType.FadeInOut) {
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            } else if (transitionType == FragmentTransitionType.SlideLeft) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right);
            } else if (transitionType == FragmentTransitionType.SlideRight) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            }

        }

        if (addType) {
            fragmentTransaction.add(parentResId, pushFragment, pushFragmentTag);
        } else {
            fragmentTransaction.replace(parentResId, pushFragment);
        }
        if (backStackType) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void setFragment(int setResourceId, TomasFragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        ArrayList<Fragment> lists = (ArrayList<Fragment>) fm.getFragments();

        TomasFragment homeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);

        if (homeFragment != null) {
            if (lists != null) {
                for (Fragment fragments : lists) {
                    fm.beginTransaction().hide(fragments).commitAllowingStateLoss();
                }
            }
            fm.beginTransaction().show(homeFragment).commitAllowingStateLoss();
        } else {
            if (lists != null) {
                for (Fragment fragments : lists) {
                    fm.beginTransaction().hide(fragments).commitAllowingStateLoss();
                }
            }
            FragmentTransaction fts = fm.beginTransaction();
            fts.add(setResourceId, fragment, fragmentTag);
            fts.commitAllowingStateLoss();
        }
    }


    public void showFragment(int setResourceId, TomasFragment fragment, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        TomasFragment homeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);

        if (homeFragment != null && !homeFragment.isRemoving()) {
            fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        } else {
            FragmentTransaction fts = fm.beginTransaction();
            fts.add(setResourceId, fragment, fragmentTag);
            fts.addToBackStack(null);
            fts.commitAllowingStateLoss();
        }
    }

    public void hideFragment(String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        TomasFragment homeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);
        if (homeFragment == null) return;
        FragmentTransaction fts = fm.beginTransaction();
        fts.hide(homeFragment);
        fts.commit();
    }

    public void removeFragment(String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        TomasFragment removeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);
        if (removeFragment == null) return;
        FragmentTransaction fts = fm.beginTransaction();
        fts.remove(removeFragment);
        fts.commitAllowingStateLoss();
    }

    public void setChildFragment(int setResourceId, TomasFragment parentFragment, TomasFragment fragment) {
        setChildFragment(setResourceId, parentFragment, fragment, FragmentTransitionType.Default);
    }

    public void setChildFragment(int setResourceId, TomasFragment parentFragment, TomasFragment fragment, FragmentTransitionType transitionType) {
        setChildFragment(setResourceId, parentFragment, fragment, transitionType, null);
    }

    public void setChildFragment(int setResourceId, TomasFragment parentFragment, TomasFragment fragment, FragmentTransitionType transitionType, String fragmentTag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (AppContext.getInstance().getActivity().isDestroyed()) return;
        }


        FragmentManager fm = parentFragment.getChildFragmentManager();
        ArrayList<Fragment> lists = (ArrayList<Fragment>) fm.getFragments();

        if (lists != null) {
            for (Fragment fragments : lists) {
                fm.beginTransaction().remove(fragments).commitAllowingStateLoss();
            }
        }
        FragmentTransaction fts = fm.beginTransaction();
        if (transitionType != FragmentTransitionType.Default) {
            if (transitionType == FragmentTransitionType.FadeInOut) {
                fts.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            } else if (transitionType == FragmentTransitionType.SlideLeft) {
                fts.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_out_right);
            } else if (transitionType == FragmentTransitionType.SlideRight) {
                fts.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            }
        }
        if (fragmentTag == null) {
            fts.replace(setResourceId, fragment);
        } else {
            fts.add(setResourceId, fragment, fragmentTag);
        }
        try {
            fts.commitAllowingStateLoss();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }


    public void showChildFragment(int setResourceId, TomasFragment parentFragment, TomasFragment fragment, String fragmentTag) {
        FragmentManager fm = parentFragment.getChildFragmentManager();
        TomasFragment homeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);

        if (homeFragment != null && !homeFragment.isRemoving()) {
            fm.beginTransaction().show(fragment).commit();
        } else {
            FragmentTransaction fts = fm.beginTransaction();
            fts.add(setResourceId, fragment, fragmentTag);
            fts.addToBackStack(null);
            fts.commit();
        }
    }

    public void removeChildFragment(TomasFragment parentFragment, String fragmentTag) {
        FragmentManager fm = parentFragment.getChildFragmentManager();
        TomasFragment removeFragment = (TomasFragment) fm.findFragmentByTag(fragmentTag);
        if (removeFragment == null) return;
        FragmentTransaction fts = fm.beginTransaction();
        fts.remove(removeFragment);
        fts.commit();
    }

    private Display getDisplay() {
        Display dsp = null;
        WindowManager wm = (WindowManager) mApplicationContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return dsp;
        dsp = wm.getDefaultDisplay();
        return dsp;
    }

    /**
     * @return 세로화면 사이즈 호출
     */
    private Rect getPortraitRect() {
        Rect portraitRect;
        if (mPortraitScreenRect == null) {
            portraitRect = new Rect();
            int portraitWidth = AppPreferenceManager.getInstance().getInt(PORTRAIT_WIDTH);
            int portraitHeight = AppPreferenceManager.getInstance().getInt(PORTRAIT_HEIGHT);
            if (portraitWidth == AppPreferenceManager.INT_DEFAULT) {
                portraitWidth = getWidthOfDisplay();
                AppPreferenceManager.getInstance().put(PORTRAIT_WIDTH, portraitWidth);
            }
            if (portraitHeight == AppPreferenceManager.INT_DEFAULT) {
                portraitHeight = getHeightOfDisplay() - getStatusBarHeight();
                AppPreferenceManager.getInstance().put(PORTRAIT_HEIGHT, portraitHeight);
            }
            portraitRect.left = SIZE_ZERO;
            portraitRect.top = SIZE_ZERO;
            portraitRect.right = portraitWidth;
            portraitRect.bottom = portraitHeight;
        } else {
            portraitRect = mPortraitScreenRect;
        }
        return portraitRect;
    }

    /**
     * @return 가로 화면 사이즈 호출
     */
    private Rect getLandscapeRect() {
        Rect landscapeRect;
        if (mLandScapeScreenRect == null) {
            landscapeRect = new Rect();
            int landscapeWidth = AppPreferenceManager.getInstance().getInt(LANDSCAPE_WIDTH);
            int landscapeHeight = AppPreferenceManager.getInstance().getInt(LANDSCAPE_HEIGHT);
            if (landscapeWidth == AppPreferenceManager.INT_DEFAULT) {
                landscapeWidth = getHeightOfDisplay();
                AppPreferenceManager.getInstance().put(LANDSCAPE_WIDTH, landscapeWidth);
            }
            if (landscapeHeight == AppPreferenceManager.INT_DEFAULT) {
                landscapeHeight = getWidthOfDisplay() - getStatusBarHeight();
                AppPreferenceManager.getInstance().put(LANDSCAPE_HEIGHT, landscapeHeight);
            }
            landscapeRect.left = SIZE_ZERO;
            landscapeRect.top = SIZE_ZERO;
            landscapeRect.right = landscapeWidth;
            landscapeRect.bottom = landscapeHeight;
        } else {
            landscapeRect = mLandScapeScreenRect;
        }
        return landscapeRect;
    }

    /**
     * @return 상단 스테이터스바 사이즈 호출
     */
    private int getStatusBarHeight() {
        int statusBar = SIZE_ZERO;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > SIZE_ZERO) {
            statusBar = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBar;
    }

    private int getWidthOfDisplay() {
        if (mApplicationContext == null) return SIZE_ZERO;
        Display dp = getDisplay();
        if (dp == null) return SIZE_ZERO;
        return dp.getWidth();
    }

    private int getHeightOfDisplay() {
        if (mApplicationContext == null) return SIZE_ZERO;
        Display dp = getDisplay();
        if (dp == null) return SIZE_ZERO;
        return dp.getHeight();
    }

    private Resources getResources() {
        if (mApplicationContext == null) return null;
        return mApplicationContext.getResources();
    }


}
