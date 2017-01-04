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

import java.util.Timer;
import java.util.TimerTask;

import uk.ac.mdx.cs.ie.acontextlib.LocationObserver;

/**
 * Calculates distanced travelled continously
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class DistanceTravelledObserver extends LocationObserver {

    private Timer mTimer;
    private int mInteval = 30000;
    private Location mLastLocation = null;
    private float mTotalDistance;
    public static final String RECEIVER_DISTANCE_TRAVELLED = "device.distancetravelled";

    public DistanceTravelledObserver(Context c) {
        super(c, 3000, 0, "DistanceTravelledObserver");
    }

    public void checkContext(Object data) {

        Location newLocation = (Location) data;

        if (mLastLocation != null) {
            float distance = mLastLocation.distanceTo(newLocation);
            synchronized (this) {
                mTotalDistance += distance;
            }
        }

        mLastLocation = newLocation;

    }

    @Override
    public boolean start() {

        mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendUpdate();
            }
        }, mInteval, mInteval);

        return super.start();
    }

    private synchronized void sendUpdate() {
        long distance = Math.round(mTotalDistance);
        sendToContextReceivers(RECEIVER_DISTANCE_TRAVELLED, distance);
        mTotalDistance = 0;
    }

    @Override
    public boolean stop() {
        mTimer.cancel();
        return super.stop();
    }
}
