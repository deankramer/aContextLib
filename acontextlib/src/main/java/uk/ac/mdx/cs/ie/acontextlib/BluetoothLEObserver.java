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

package uk.ac.mdx.cs.ie.acontextlib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import uk.ac.mdx.cs.ie.acontextlib.utility.BTScanner;
import uk.ac.mdx.cs.ie.acontextlib.utility.PostLolliBTScanner;
import uk.ac.mdx.cs.ie.acontextlib.utility.PreLolliBTScanner;

/**
 * Abstract class for Bluetooth LE Devices on Pre Lollipop
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BluetoothLEObserver extends PushObserver {

    private BluetoothGatt mBluetoothGatt;
    private ArrayList<UUID> mInterestedServices;
    private ArrayList<UUID> mInterestedMeasurements;
    private String mDeviceID;
    private UUID mPhoneID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final String LOG_TAG = "BluetoothLEObserver";
    private BTScanner mScanner;

    public BluetoothLEObserver(Context c) {

        super(c);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScanner = new PostLolliBTScanner(c, this, mLeScanCallback);
        } else {
            mScanner = new PreLolliBTScanner(c, this, mLeScanCallback);
        }
    }

    public BluetoothLEObserver(Context c, UUID service,
                               UUID measurement) {

        this(c);

        mInterestedServices = new ArrayList<>();
        mInterestedMeasurements = new ArrayList<>();

        mInterestedServices.add(service);
        mInterestedMeasurements.add(measurement);

    }

    public BluetoothLEObserver(Context c, ArrayList<UUID> services,
                               ArrayList<UUID> measurements) {

        this(c);

        mInterestedServices = services;
        mInterestedMeasurements = measurements;

    }

    public boolean hasGatt() {
        if (mBluetoothGatt == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean setContextParameters(HashMap<String, Object> parameters) {
        if (super.setContextParameters(parameters)) {
            return true;
        } else {
            return false;
        }
    }

    public void setDeviceID(String device) {
        mDeviceID = device;
    }


    @CallSuper
    @Override
    public synchronized boolean start() {

        if (mIsRunning) {
            return false;
        }

        if (!hasPermission()) {
            return false;
        }

        if (!mScanner.isEnabled() || mDeviceID.isEmpty()) {
            return false;
        }

        mScanner.scanForLeDevice(true);
        mIsRunning = true;

        return true;
    }

    @CallSuper
    @Override
    public boolean pause() {

        return stop();
    }

    @CallSuper
    @Override
    public boolean resume() {

        return start();
    }

    @CallSuper
    @Override
    public synchronized boolean stop() {
        if (mIsRunning) {
            if (mBluetoothGatt == null) {
                return false;
            }

            mScanner.scanForLeDevice(false);

            mIsRunning = false;

            mBluetoothGatt.close();
            mBluetoothGatt = null;

            return true;
        } else {
            return false;
        }
    }

    private BTScanner.BTScannerCallback mLeScanCallback = new BTScanner.BTScannerCallback() {
        @Override
        public void foundDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getAddress().equals(mDeviceID)) {
                mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
            }
        }

        @Override
        public void connectionChange(boolean connected) {
            BluetoothLEObserver.this.connectionChange(connected);
        }
    };

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.v(LOG_TAG, "Connected to device");
                connectionChange(true);
                gatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.v(LOG_TAG, "Disconnected from device, " +
                        "will attempt to reconnect if observer running");
                connectionChange(false);

                if (mIsRunning) {
                    gatt.connect();
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {

                for (UUID interestedService : mInterestedServices) {
                    BluetoothGattService service = gatt.getService(interestedService);

                    if (service != null) {

                        for (UUID interestedMeasurement : mInterestedMeasurements) {
                            BluetoothGattCharacteristic characteristic = service.getCharacteristic(interestedMeasurement);

                            if (characteristic != null) {

                                gatt.setCharacteristicNotification(characteristic, true);
                                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(mPhoneID);
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                gatt.writeDescriptor(descriptor);
                            }
                        }

                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            checkContext(characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            checkContext(characteristic);
        }
    };

    public abstract void connectionChange(boolean connected);
}
