package uk.ac.mdx.cs.ie.acontextlib.utility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import uk.ac.mdx.cs.ie.acontextlib.BluetoothLEObserver;

/**
 * Abstract Scanner class for Bluetooth LE Devices
 *
 * @author Dean Kramer <deankramer99@gmail.com>
 */

public abstract class BTScanner {

    public interface BTScannerCallback {
        void foundDevice(BluetoothDevice device, int rssi, byte[] scanRecord);
        void connectionChange(boolean connected);
    }

    protected static final long SCAN_PERIOD = 10000;
    protected BluetoothAdapter mBluetoothAdapter;
    protected Handler mHandler = new Handler();
    protected boolean mScanning;
    protected boolean mConnectRetry = false;
    protected BTScannerCallback mCallback;
    protected BluetoothLEObserver mObserver;

    public void setCallback(BTScannerCallback callback) {
        mCallback = callback;
    }

    public void setConnectRetry(boolean retry) {
        mConnectRetry = retry;
    }

    public boolean isScanning() {return  mScanning;}

    public boolean isEnabled() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        } else {
            return false;
        }
    }

    public abstract void scanForLeDevice(final boolean enable);

}
