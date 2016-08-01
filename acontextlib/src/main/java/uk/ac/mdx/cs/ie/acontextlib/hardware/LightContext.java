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

import uk.ac.mdx.cs.ie.acontextlib.SensorContext;

/**
 * Gets the level of light lumens
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class LightContext extends SensorContext {

    private long mCurrentValue;
    private long mContextDifferenceHigher = 0;
    private long mContextDifferenceLower = 0;
    public static final String RECEIVER_LIGHT = "sensor.light_lumens";

    public LightContext(Context c) {
        super(c, Sensor.TYPE_LIGHT, SensorManager.SENSOR_DELAY_NORMAL, "LightContext");

    }

    public LightContext(Context c, long difference) {
        this(c);
        mContextDifferenceHigher = difference;
    }

    public void setContextDifference(long difference) {
        mContextDifferenceHigher = difference;
    }

    @Override
    protected void checkContext(float[] values) {
        long value = Math.round(values[0]);

        long difference = Math.abs(mCurrentValue - value);
        long threshold;
        if (value > mCurrentValue) {
            threshold = mContextDifferenceHigher;
        } else {
            threshold = mContextDifferenceLower;
        }


        if (difference >= threshold) {
            mCurrentValue = value;
            sendToContextReceivers(RECEIVER_LIGHT, mCurrentValue);
            mContextDifferenceHigher = value * 2;
            mContextDifferenceLower = value / 2;
        }

    }


}
