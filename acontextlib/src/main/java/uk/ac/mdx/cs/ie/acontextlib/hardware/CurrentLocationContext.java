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
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import uk.ac.mdx.cs.ie.acontextlib.LocationContext;

/**
 * Gets the current device location
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class CurrentLocationContext extends LocationContext {

    private boolean mIsPassive = false;
    public static final String RECEIVER_CURRENT_LOCATION = "device.current_loc";

    public CurrentLocationContext(Context c, String provider) {
        super(c, provider, "CurrentLocationContext");

        if (provider.equals(LocationManager.PASSIVE_PROVIDER)) {
            mIsPassive = true;
        }
    }


    public void checkContext(Object object) {
        sendToContextReceivers(RECEIVER_CURRENT_LOCATION, object);
    }

    public void changeProvider(boolean passive) {

        if (passive) {
            setIdealProvider(LocationManager.PASSIVE_PROVIDER);
        } else {
            setIdealProvider(LocationManager.NETWORK_PROVIDER);
        }

        mIsPassive = passive;

    }

    public Thread getCurrentLocation() {

        Thread t = null;

        if (mIsPassive) {

            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                                CurrentLocationContext.this, null);
                        Looper.loop();
                    }
                });

                t.start();
            }
        }

        return t;
    }


    public Location getLastKnownLocation(String provider) {
        return mLocationManager.getLastKnownLocation(provider);
    }
}
