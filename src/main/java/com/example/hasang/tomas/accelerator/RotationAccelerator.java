package com.example.hasang.tomas.accelerator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.example.hasang.tomas.AppContext;

/**
 * Created by hasang on 16. 1. 25..
 */
public class RotationAccelerator implements SensorEventListener {
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Handler handler;
    private OnTagAngleChangedListener onTagAngleChangedListener;
    private boolean paused = false;

    public interface OnTagAngleChangedListener {
        void onTagAngleChanged();
    }


    private float tagAngleDegreen;  //회전 값
    private double tagAngleRadians;

    private double angularVelocity;

    private Context context;

    private VelocityTracker velocityTracker;

    Runnable updater = new Runnable() {
        public void run() {
            if (!paused) {
                try {
                    recalculateAngle();
                    handler.postDelayed(updater, 50l);    //핸들러 재귀 호출
                    return;
                } catch (Exception localException) {
                }
            }
        }
    };

    public RotationAccelerator(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        accelerometer = this.sensorManager.getDefaultSensor(1);
        handler = new Handler();
    }

    //앱 회전 시작
    public void resume() {
        startRotation();
    }

    private void startRotation() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        sensorManager.registerListener(this, accelerometer, 0); //센서 이밴트 제요청
        //데이터 초기화
        paused = false;
        //핸들러 처음 시작
        handler.removeCallbacks(updater);
        handler.postDelayed(updater, 50L);
    }

    //앱 종료
    public void destroy() {
        stopRotaion();
    }

    private VelocityTracker getVelocityTracker() {
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain();
        return velocityTracker;
    }

    public void onScroll(MotionEvent paramMotionEvent) {
        getVelocityTracker().addMovement(paramMotionEvent);
        getVelocityTracker().computeCurrentVelocity(1000, 100000.0F);
        this.angularVelocity += getVelocityTracker().getXVelocity() / 1200.0F;

    }

    //앱 중지시
    public void pause() {
        stopRotaion();
    }

    private void stopRotaion() {
        paused = true;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    //이벤트 추가
    public void addTagAngleListener(OnTagAngleChangedListener paramOnTagAngleChangedListener) {
        onTagAngleChangedListener = paramOnTagAngleChangedListener;
    }

    //현재 회전 량 가져오기
    public float getCurrentAngleDegrees() {
        return tagAngleDegreen;
    }

    //회전 가져오도록 인터페이스 구현
    protected void notifyAngleChanged() {
        if (onTagAngleChangedListener != null)
            onTagAngleChangedListener.onTagAngleChanged();
    }

    public void recalculateAngle() {
        double d1 = 0.0;
        float rotationvalue;

        double theta = Math.atan2(accelerationY, accelerationX);    //중력 각도

        double degree = ((theta * -180.0) / 3.14159) + 180;  // +180 to keep 0 on the right


        rotationvalue = (float) ((degree + 270.0) % 360.0);

        //눕혔을때
        if ((Math.abs(accelerationZ) > (3.5D * Math.abs(accelerationX))) && (Math.abs(accelerationZ) > (3.5D * Math.abs(accelerationY)))) {
            rotationvalue = 0;
        }

        int i = AppContext.getInstance().getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int orientation = 0;

        switch (i) {
            case 1:
                orientation = 90;

                break;

            case 3:
                orientation = -90;

                break;
        }
        d1 = tagAngleRadians - rotationvalue;


        if (d1 <= -180) {
            d1 = d1 + 360;
        } else if (d1 >= 180) {
            d1 = d1 - 360;
        }

        angularVelocity -= (0.09 * d1);
        angularVelocity *= 0.9;


        tagAngleRadians += angularVelocity;

        tagAngleDegreen = (float) tagAngleRadians - orientation;

        if (Math.abs(angularVelocity) > 1) {
            notifyAngleChanged();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent paramSensorEvent) {
        accelerationX = paramSensorEvent.values[0];
        accelerationY = paramSensorEvent.values[1];
        accelerationZ = paramSensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
