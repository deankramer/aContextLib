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

import uk.ac.mdx.cs.ie.acontextlib.SensorObserver;

/**
 * Gathers the curent orientation of the user.
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class CompassObserver extends SensorObserver {


    private long mCurrentDegrees;
    public static final String RECEIVER_COMPASS = "sensor.compass_degrees";

    public CompassObserver(Context c) {
        //super(c, cr, Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_NORMAL, "CompassObserver");
        super(c, Sensor.TYPE_ORIENTATION, SensorManager.SENSOR_DELAY_NORMAL, "CompassObserver");
    }


    @Override
    protected void checkContext(float[] values) {
        long degree = Math.round(values[0]);
        if (degree != mCurrentDegrees) {
            mCurrentDegrees = degree;
            sendToContextReceivers(RECEIVER_COMPASS, mCurrentDegrees);
        }

    }

}
