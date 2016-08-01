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

package uk.ac.mdx.cs.ie.acontextlib.personal;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Build;

import java.util.UUID;

import uk.ac.mdx.cs.ie.acontextlib.BluetoothLEDevice;

/**
 * Gets heart rates from a bluetooth monitor.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class HeartRateMonitor extends BluetoothLEDevice {

    public final static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public final static String HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";
    public final static String RECEIVER_HEARTRATE = "sensor.heartrate";
    public final static String RECEIVER_HEARTRATE_CONNECTED = "sensor.heartrate.connected";

    public HeartRateMonitor(Context c) {
        super(c, UUID.fromString(HEART_RATE_SERVICE), UUID.fromString(HEART_RATE_MEASUREMENT));
    }

    public void checkContext(Object data) {

        BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;

        int flag = characteristic.getProperties();
        int format = -1;

        if ((flag & 0x01) != 0) {
            format = BluetoothGattCharacteristic.FORMAT_UINT16;
        } else {
            format = BluetoothGattCharacteristic.FORMAT_UINT8;
        }

        final int heartRate = characteristic.getIntValue(format, 1);

        sendToContextReceivers(RECEIVER_HEARTRATE, heartRate);
    }

    @Override
    public void connectionChange(boolean connected) {
        sendToContextReceivers(RECEIVER_HEARTRATE_CONNECTED, connected);
    }
}
