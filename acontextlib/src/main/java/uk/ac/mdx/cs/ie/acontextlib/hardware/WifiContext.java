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
import android.net.wifi.WifiManager;
import android.os.Bundle;

import uk.ac.mdx.cs.ie.acontextlib.BroadcastContext;

/**
 * Provides Wifi State messages (contecting
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class WifiContext extends BroadcastContext {


    private final WifiManager mWifiManager;
    private int mWifiState;

    public WifiContext(Context c) {
        super(c, WifiManager.NETWORK_STATE_CHANGED_ACTION, "WifiContext");
        mWifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void checkContext(Bundle data) {
        int newState = mWifiManager.getWifiState();
        if (newState != mWifiState) {
            mWifiState = newState;

            String state = wifiStateToString(newState);

            sendToContextReceivers("sensor.wifi_state", state);
        }
    }

    public static String wifiStateToString(int wifiState) {

        String state;

        switch (wifiState) {
            case 1:
                state = "WIFI_STATE_DISABLED";
                break;
            case 0:
                state = "WIFI_STATE_DISABLING";
                break;
            case 3:
                state = "WIFI_STATE_ENABLED";
                break;
            case 2:
                state = "WIFI_STATE_ENABLING";
                break;
            default:
                state = "WIFI_STATE_UNKNOWN";
                break;
        }

        return state;
    }


}
