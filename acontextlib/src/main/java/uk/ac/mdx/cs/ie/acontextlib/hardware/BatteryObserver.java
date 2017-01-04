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
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;

import uk.ac.mdx.cs.ie.acontextlib.BroadcastObserver;


/**
 * Gets Battery Level
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class BatteryObserver extends BroadcastObserver {

    private int mBatteryLevel;
    public static final String RECEIVER_BATTERY = "sensor.battery_level";


    public BatteryObserver(Context c) {
        super(c, Intent.ACTION_BATTERY_CHANGED, "BatteryObserver");
    }


    protected void checkContext(Bundle bundle) {

        int rawlevel = bundle.getInt(BatteryManager.EXTRA_LEVEL, -1);
        int scale = bundle.getInt(BatteryManager.EXTRA_SCALE, -1);

        if (rawlevel >= 0 && scale > 0) {
            mBatteryLevel = (rawlevel * 100) / scale;

            //Send the receiver the context update
            sendToContextReceivers(RECEIVER_BATTERY, mBatteryLevel);
        }
    }

    public int getBatteryLevel() {
        return mBatteryLevel;
    }

}
