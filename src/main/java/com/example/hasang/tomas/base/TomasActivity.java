package com.example.hasang.tomas.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hasang.tomas.AppContext;

/**
 * Created by hasang on 16. 8. 1..
 */
public class TomasActivity extends AppCompatActivity {

    private OnKeyBackPressedListener onKeyBackPressedListener;

    public interface OnKeyBackPressedListener {
        public void onBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.getInstance().setActivity(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 백 버튼에 대한 리스너 처리
     * 리스너가 없을 경우 정상적인 종료 작업을 실행하며
     * 리스너가 있을 경우 해당 리스너에 대한 행동 처리
     */
    @Override
    public void onBackPressed() {
        if (onKeyBackPressedListener != null) {
            onKeyBackPressedListener.onBack();
        } else {
            finish();
        }
    }

    /**
     * 백 버튼 클릭시에 실행될 이벤트 입력
     *
     * @param onKeyBackPressedListener 백 버튼 클릭시에 실행될 이벤트
     */
    public void setOnKeyBackPressedListener(OnKeyBackPressedListener onKeyBackPressedListener) {
        this.onKeyBackPressedListener = onKeyBackPressedListener;
    }


}
