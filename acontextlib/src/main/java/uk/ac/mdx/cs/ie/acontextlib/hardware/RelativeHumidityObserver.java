/*
 * Copyright 2017 aContextLib
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
 * Gets Relative Humidity
 *
 * @author Dean Kramer <deankramer99@gmail.com>
 */

public class RelativeHumidityObserver extends SensorObserver {

    private int mHumidityValue;
    public static final String RECEIVER_RELATIVE_HUMIDITY = "sensor.relative_humidity";

    public RelativeHumidityObserver(Context c) {
        super(c, Sensor.TYPE_RELATIVE_HUMIDITY, SensorManager.SENSOR_DELAY_NORMAL, "RelativeHumidityObserver");
    }

    @Override
    protected void checkContext(float[] values) {
        int value = Math.round(values[0]);

        if (value != mHumidityValue) {
            mHumidityValue = value;

            sendToContextReceivers(RECEIVER_RELATIVE_HUMIDITY, mHumidityValue);
        }
    }
}
