package uk.ac.mdx.cs.ie.acontextlib.utility;

import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

import uk.ac.mdx.cs.ie.acontextlib.BluetoothLEObserver;

/**
 * Scanner class for Bluetooth LE Devices for Post-Lollipop devices
 *
 * @author Dean Kramer <deankramer99@gmail.com>
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PostLolliBTScanner extends BTScanner {

    private BluetoothLeScanner mBluetoothScanner;

    public PostLolliBTScanner(Context c, BluetoothLEObserver observer) {

        mObserver = observer;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) c.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    public PostLolliBTScanner(Context c, BluetoothLEObserver observer, BTScannerCallback callback) {
        this(c, observer);
        mCallback = callback;
    }

    public void scanForLeDevice(final boolean enable) {
        if (enable) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothScanner.stopScan(mLeScanCallback);

                    if ( (! mObserver.hasGatt()) && mConnectRetry) {
                        mCallback.connectionChange(false);
                        scanForLeDevice(enable);
                    } else {
                        mCallback.connectionChange(true);
                    }

                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothScanner.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothScanner.stopScan(mLeScanCallback);
        }
    }

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            mCallback.foundDevice(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                onScanResult(0, result);
            }
        }
    };
}
