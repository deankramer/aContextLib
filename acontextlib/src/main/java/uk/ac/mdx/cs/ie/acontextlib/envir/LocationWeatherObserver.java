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

package uk.ac.mdx.cs.ie.acontextlib.envir;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.HashMap;

import uk.ac.mdx.cs.ie.acontextlib.ContextException;
import uk.ac.mdx.cs.ie.acontextlib.PullObserver;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.openweathermap.OpenWeatherMapSource;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Weather;

/**
 * Monitors Weather Data for specific locations
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class LocationWeatherObserver extends PullObserver {

    private OpenWeatherMapSource mWeatherSource;
    private Weather mCurrentWeather;
    private String mPlace;
    private Location mLocationPlace;
    private boolean mFirstTime = true;
    private boolean mIsStrings = true;
    public static final String RECEIVER_WEATHER = "weather";

    public LocationWeatherObserver(Context c) {
        super(c, 2000, "LocationWeatherObserver");
        mWeatherSource = new OpenWeatherMapSource(c);
    }

    public LocationWeatherObserver(Context c, String place) {
        this(c);
        mPlace = place;
    }

    public LocationWeatherObserver(Context c, Location place) {
        this(c);
        mLocationPlace = place;
        mIsStrings = false;
    }

    @Override
    public boolean setContextParameters(HashMap<String, Object> parameters) {
        if (super.setContextParameters(parameters)) {
            String stringPlace = (String) parameters.get("place");
            Location locationPlace = (Location) parameters.get("location");
            if (stringPlace != null && locationPlace == null) {
                setPlace(stringPlace);
                Log.e(mName, "setting place para with: " + stringPlace);
                return true;
            } else if (stringPlace == null && locationPlace != null) {
                setPlace(locationPlace);
                Log.e(mName, "setting location para with: " + locationPlace.toString());
                return true;
            } else {
                Log.e(mName, "You shouldn't send both Location and String places!");
                return false;
            }
        } else {
            return false;
        }

    }


    public void checkContext() {
        try {

            if (mIsStrings) {
                mCurrentWeather = mWeatherSource.query(mPlace, 0);
            } else {
                mCurrentWeather = mWeatherSource.query(mLocationPlace, 0);
            }

            sendToContextReceivers(RECEIVER_WEATHER, mCurrentWeather);

            if (mFirstTime) {
                mFirstTime = false;
                //Check every 30 mins from now.
                setInterval(1800000);
            }
        } catch (ContextException e) {
            Log.e(mName, e.toString());
        }

    }

    @Override
    public boolean start() {
        if (mPlace == null && mLocationPlace == null) {
            Log.e(mName, "No location/place to monitor!");
            return false;
        } else {
            return super.start();
        }
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
        mLocationPlace = null;
        mIsStrings = true;
    }

    public void setPlace(Location place) {
        mLocationPlace = place;
        mPlace = null;
        mIsStrings = false;
    }

}
