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

import android.content.Context;
import android.support.annotation.CallSuper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract class to hold everything required by pull/timer based context observers
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public abstract class PullObserver extends ContextObserver {

    private Timer mTimer;
    private int mInterval = 2000; //default every 2 seconds


    public PullObserver(Context c) {
        super(c);
    }

    public PullObserver(Context c, int interval, String name) {
        super(c, name);
        mInterval = interval;
    }

    public int getInterval() {
        return mInterval;
    }

    public void setInterval(int interval) {
        mInterval = interval;

        if (mIsRunning) {
            stop();
            start();
        }
    }

    @CallSuper
    @Override
    public boolean resume() {
        return start();
    }

    @CallSuper
    @Override
    public boolean pause() {
        return stop();

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

        //If there is no interval, we consider they do not want a timer.
        if (mInterval > 0) {
            mTimer = new Timer();

            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    checkContext();
                }
            }, mInterval, mInterval);
        }

        mIsRunning = true;

        return true;

    }

    public abstract void checkContext();

    @CallSuper
    @Override
    public synchronized boolean stop() {
        if (mIsRunning) {
            mTimer.cancel();
            mIsRunning = false;
            return true;
        } else {
            return false;
        }
    }

}
