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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Abstract class to hold everything required by broadcast receiving context components
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public abstract class BroadcastContext extends PushObserver {


    private BroadcastReceiver mContextMonitor = null;
    protected String mIntentFilter = "";


    public BroadcastContext(Context c) {
        super(c);
        setupMonitor();
    }

    public BroadcastContext(Context c, String filter, String name) {
        super(c, name);
        mIntentFilter = filter;
        setupMonitor();
    }


    private void setupMonitor() {
        mContextMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent in) {
                if (in.getAction().equals(mIntentFilter)) {
                    Bundle data = in.getExtras();
                    checkContext(data);
                }
            }
        };
    }

    public void checkContext(Object data) {
        if (data != null) {
            if (data instanceof Bundle) {
                checkContext((Bundle) data);
            }
        } else {
            checkContext(new Bundle());
        }

    }

    protected abstract void checkContext(Bundle data);

    @Override
    public boolean resume() {
        return start();
    }

    @Override
    public boolean pause() {
        return stop();
    }

    @Override
    public boolean start() {
        Intent currentValue = mContext.registerReceiver(mContextMonitor, new IntentFilter(mIntentFilter));
        if (currentValue != null) {
            checkContext(currentValue.getExtras());
        }

        mIsRunning = true;

        return true;
    }

    @Override
    public boolean stop() {
        mContext.unregisterReceiver(mContextMonitor);
        mIsRunning = false;
        return true;
    }

}
