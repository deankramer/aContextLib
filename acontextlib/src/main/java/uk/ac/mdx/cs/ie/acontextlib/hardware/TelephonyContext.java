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
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import uk.ac.mdx.cs.ie.acontextlib.BroadcastContext;

/**
 * Handles telephone related contexts including:
 * The state of the connection (disconnected, connecting, connected etc)
 * Whether the device is currently roaming
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class TelephonyContext extends BroadcastContext {

    private boolean mRoaming = false;
    private int mConnectionState = -1;
    public TelephonyManager mPhoneManager;
    public static final String RECEIVER_TELEPHONY_ROAMING = "sensor.telephony_roaming";
    public static final String RECEIVER_TELEPHONY_CONSTATE = "sensor.telephone_connectionstate";

    public TelephonyContext(Context c) {
        super(c, ConnectivityManager.CONNECTIVITY_ACTION, "TelephonyContext");
        mPhoneManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public boolean start() {
        sendToContextReceivers(RECEIVER_TELEPHONY_ROAMING, String.valueOf(mPhoneManager.isNetworkRoaming()));
        checkConnectionState();
        return super.start();
    }

    @Override
    protected void checkContext(Bundle data) {
        checkRoaming();
        checkConnectionState();
    }

    private void checkRoaming() {
        if (mRoaming != mPhoneManager.isNetworkRoaming()) {
            mRoaming = !mRoaming;
            sendToContextReceivers(RECEIVER_TELEPHONY_ROAMING, String.valueOf(mRoaming));
        }
    }

    private void checkConnectionState() {
        int v = mPhoneManager.getDataState();
        if (v != mConnectionState) {
            mConnectionState = v;
            sendToContextReceivers(RECEIVER_TELEPHONY_CONSTATE, getConnectionValue(mConnectionState));
        }
    }

    private static String getConnectionValue(int connectionState) {

        String state;

        switch (connectionState) {
            case TelephonyManager.DATA_CONNECTING:
                state = "CONNECTING";
                break;
            case TelephonyManager.DATA_CONNECTED:
                state = "CONNECTED";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                state = "SUSPENDED";
                break;
            default:
                state = "DISCONNECTED";
                break;
        }

        return state;
    }

    public int getConnectionState() {
        return mConnectionState;
    }

    public boolean isRoaming() {
        return mRoaming;
    }
}
