/*
 * Copyright 2016 Middlesex University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.mdx.cs.ie.acontextlib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.util.Log;

/**
 * Abstract class to hold everything required by sensor based context components
 * Interval time is by default SensorManager.SENSOR_DELAY_NORMAL
 * Max Internal time (used for batching if required) is by default 0
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public abstract class SensorContext extends PushObserver implements SensorEventListener {

    private SensorManager mSensorManager;
    private int mSensorType = -2;
    private Sensor mSensor;
    private int mInterval = SensorManager.SENSOR_DELAY_NORMAL;
    private int mMaxInterval = 0;

    public SensorContext(Context c) {
        super(c);
        mSensorManager = (SensorManager) c
                .getSystemService(Context.SENSOR_SERVICE);
    }

    public SensorContext(Context c, int sensorType, int interval, String name) {
        super(c, name);
        mSensorType = sensorType;
        mInterval = interval;
        mSensorManager = (SensorManager) c
                .getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(sensorType);
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        checkContext(event);
    }

    protected abstract void checkContext(float[] values);

    public void checkContext(Object data) {

        SensorEvent event = (SensorEvent) data;

        if (event.sensor.getType() == mSensorType) {
            checkContext(event.values);
        }
    }

    @CallSuper
    @Override
    public boolean resume() {
        return start();
    }

    @CallSuper
    @Override
    public boolean pause() {
        return stop();
    }

    @CallSuper
    @Override
    public synchronized boolean start() {
        if (mIsRunning) {
            return false;
        }

        if (!hasPermission()) {
            return false;
        }

        if (mSensorType != -2) {
            if (mSensor != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mSensorManager.registerListener(this, mSensor, mInterval, mMaxInterval);
                } else {
                    mSensorManager.registerListener(this, mSensor, mInterval);
                }
                mIsRunning = true;
                return true;
            } else {
                Log.e(mName, "Sensor does not exist on device!");
                return false;
            }

        } else {
            Log.e(mName, "Sensor Type not set!");
            return false;
        }

    }

    public void setSensorType(int sensorType) {
        mSensorType = sensorType;
        mSensor = mSensorManager.getDefaultSensor(mSensorType);
    }

    public int getSensorType() {
        return mSensorType;
    }

    public void setBatchingDelay(int maxDelay) {
        mMaxInterval = maxDelay;

    }

    @CallSuper
    @Override
    public synchronized boolean stop() {
        if (mIsRunning) {
            mSensorManager.unregisterListener(this, mSensor);
            mIsRunning = false;
            return true;
        } else {
            return false;
        }
    }
}
