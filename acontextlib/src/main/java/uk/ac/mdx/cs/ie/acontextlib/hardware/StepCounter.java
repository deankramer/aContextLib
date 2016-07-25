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

package uk.ac.mdx.cs.ie.acontextlib.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

import uk.ac.mdx.cs.ie.acontextlib.SensorContext;

/**
 * Count the number of steps in a given time
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class StepCounter extends SensorContext {

    private Timer mTimer;
    private int mInterval = 20000;

    private float mLimit = 10;
    private float mLastValues[] = new float[3 * 2];
    private float mScale;
    private float mYOffset;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
    private int mCount = 0;

    public StepCounter(Context c) {
        super(c, Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_FASTEST, "StepCounter");
        setupContext();
    }

    private void setupContext() {
        int h = 480;
        mYOffset = h * 0.5f;
        mScale = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    @Override
    protected void checkContext(float[] values) {
        float vSum = 0;
        for (int i = 0; i < 3; i++) {
            final float v = mYOffset + values[i] * mScale;
            vSum += v;
        }
        int k = 0;
        float v = vSum / 3;

        float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
        if (direction == -mLastDirections[k]) {
            int extType = (direction > 0 ? 0 : 1);

            mLastExtremes[extType][k] = mLastValues[k];
            float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

            if (diff > mLimit) {

                boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                boolean isNotContra = (mLastMatch != 1 - extType);

                if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                    mCount++;
                    mLastMatch = extType;
                } else {
                    mLastMatch = -1;
                }
            }
            mLastDiff[k] = diff;
        }
        mLastDirections[k] = direction;
        mLastValues[k] = v;
    }

    @Override
    public boolean start() {
        mCount = 0;
        mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendUpdate();
            }
        }, mInterval, mInterval);

        return super.start();
    }

    private void sendUpdate() {
        sendToContextReceivers("device.stepcounter", mCount);
        mCount = 0;
    }

    @Override
    public boolean stop() {
        mTimer.cancel();
        return super.stop();
    }
}
