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

package uk.ac.mdx.cs.ie.acontextlib.utility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import uk.ac.mdx.cs.ie.acontextlib.BluetoothLEObserver;

/**
 * Scanner class for Bluetooth LE Devices on Pre-Lollipop devices
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PreLolliBTScanner extends BTScanner {

    public PreLolliBTScanner(Context c, BluetoothLEObserver observer) {

        mObserver = observer;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public PreLolliBTScanner(Context c, BluetoothLEObserver observer, BTScannerCallback callback) {
        this(c, observer);
        mCallback = callback;
    }

    public void scanForLeDevice(final boolean enable) {
        if (enable) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    if ((! mObserver.hasGatt()) && mConnectRetry) {
                        mCallback.connectionChange(false);
                        scanForLeDevice(enable);
                    } else {
                        mCallback.connectionChange(true);
                    }

                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    mCallback.foundDevice(device, rssi, scanRecord);
                }
            };
}
