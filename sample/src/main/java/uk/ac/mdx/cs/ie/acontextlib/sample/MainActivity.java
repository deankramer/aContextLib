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
import uk.ac.mdx.cs.ie.acontextlib.hardware.LightContext;

/**
 * Sample Activity to demonstrate context components.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class MainActivity extends Activity implements IContextReceiver {

    private LightContext mLightContext;
    private Context mContext;
    private TextView mLightLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLightLevel = (TextView) findViewById(R.id.txtLight);

        mContext = getApplicationContext();
        mLightContext = new LightContext(mContext);

        mLightContext.addContextReceiver(this);
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
    }

    private void stopContexts() {
        mLightContext.stop();
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
    public void newContextValue(String name, long value) {
        if (name.equals("sensor.light_lumens")) {
            mLightLevel.setText(String.valueOf(value));
        }
    }

    @Override
    public void newContextValue(String name, double value) {

    }

    @Override
    public void newContextValue(String name, boolean value) {

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

    @Override
    public void newUIEvent(int event) {

    }
}
