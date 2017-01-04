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

import uk.ac.mdx.cs.ie.acontextlib.BroadcastObserver;

/**
 * Provides Wifi State messages (contecting
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class WifiObserver extends BroadcastObserver {


    private final WifiManager mWifiManager;
    private int mWifiState;
    public static final String RECEIVER_WIFISTATE = "sensor.wifi_state";

    public WifiObserver(Context c) {
        super(c, WifiManager.NETWORK_STATE_CHANGED_ACTION, "WifiObserver");
        mWifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean start() {
        checkContext(null);
        return super.start();
    }

    @Override
    protected void checkContext(Bundle data) {
        int newState = mWifiManager.getWifiState();
        if (newState != mWifiState) {
            mWifiState = newState;

            String state = wifiStateToString(newState);

            sendToContextReceivers(RECEIVER_WIFISTATE, state);
        }
    }

    public static String wifiStateToString(int wifiState) {

        String state;

        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                state = "WIFI_STATE_DISABLED";
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                state = "WIFI_STATE_DISABLING";
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                state = "WIFI_STATE_ENABLED";
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                state = "WIFI_STATE_ENABLING";
                break;
            default:
                state = "WIFI_STATE_UNKNOWN";
                break;
        }

        return state;
    }


}
