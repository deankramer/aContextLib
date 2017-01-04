/*
 * Copyright 2016 aContextLib Project
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

package uk.ac.mdx.cs.ie.acontextlib.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.BatteryObserver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.GPSIndoorOutdoorObserver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.LightObserver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounterObserver;

/**
 * Sample Activity to demonstrate context components.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class MainActivity extends Activity implements IContextReceiver {

    private LightObserver mLightContext;
    private BatteryObserver mBatteryContext;
    private GPSIndoorOutdoorObserver mIndoorContext;
    private StepCounterObserver mStepCounter;
    private Context mContext;
    private TextView mLightLevel;
    private TextView mBatteryLevel;
    private TextView mIndoorStatus;
    private TextView mStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLightLevel = (TextView) findViewById(R.id.txtLight);
        mBatteryLevel = (TextView) findViewById(R.id.txtBattery);
        mIndoorStatus = (TextView) findViewById(R.id.txtIndoor);
        mStepCount = (TextView) findViewById(R.id.txtSteps);


        mContext = getApplicationContext();
        mLightContext = new LightObserver(mContext);
        mBatteryContext = new BatteryObserver(mContext);
        mIndoorContext = new GPSIndoorOutdoorObserver(mContext);
        mStepCounter = new StepCounterObserver(mContext);


        mLightContext.addContextReceiver(this);
        mBatteryContext.addContextReceiver(this);
        mIndoorContext.addContextReceiver(this);
        mStepCounter.addContextReceiver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            startContexts();
            return true;
        } else if (id == R.id.action_stop) {
            stopContexts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startContexts() {
        mLightContext.start();
        mBatteryContext.start();
        mIndoorContext.start();
        mStepCounter.start();
    }

    private void stopContexts() {
        mLightContext.stop();
        mBatteryContext.stop();
        mIndoorContext.stop();
        mStepCounter.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        startContexts();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopContexts();
    }

    //Context Receiver Implementations

    @Override
    public void newContextValue(String name, final long value) {
        if (name.equals(LightObserver.RECEIVER_LIGHT)) {
            mLightLevel.setText(String.valueOf(value));
        } else if (name.equals(BatteryObserver.RECEIVER_BATTERY)) {
            mBatteryLevel.setText(String.valueOf(value));
        } else if (name.equals(StepCounterObserver.RECEIVER_STEPS)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStepCount.setText(String.valueOf(value));
                }
            });
        }
    }

    @Override
    public void newContextValue(String name, double value) {

    }

    @Override
    public void newContextValue(final String name, final boolean value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (name.equals(GPSIndoorOutdoorObserver.RECEIVER_INDOOR_OUTDOOR)) {
                    mIndoorStatus.setText(String.valueOf(value));
                }
            }
        });
    }

    @Override
    public void newContextValue(String name, String value) {

    }

    @Override
    public void newContextValue(String name, Object value) {

    }

    @Override
    public void newContextValues(Map<String, String> values) {

    }

}
