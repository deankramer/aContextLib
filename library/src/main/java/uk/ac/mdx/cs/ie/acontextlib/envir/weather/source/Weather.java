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

package uk.ac.mdx.cs.ie.acontextlib.envir.weather.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weather {

    private List<WeatherPeriod> mWeatherPeriods = new ArrayList<WeatherPeriod>();
    private Date mSunrise;
    private Date mSunset;
    private Date mQueryTime;
    private String mLocation;
    private Date mTime;
    private int mCityId;

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String loc) {
        mLocation = loc;
    }

    public Date getSunrise() {
        return mSunrise;
    }

    public void setSunrise(Date sunrise) {
        mSunrise = sunrise;
    }

    public Date getSunset() {
        return mSunset;
    }

    public void setSunset(Date sunset) {
        mSunset = sunset;
    }

    public List<WeatherPeriod> getWeatherPeriods() {
        return mWeatherPeriods;
    }

    public void setWeatherPeriods(List<WeatherPeriod> weatherPeriods) {
        mWeatherPeriods = weatherPeriods;
    }

    public void setQueryTime(Date queryTime) {
        mQueryTime = queryTime;
    }

    public Date getQueryTime() {
        return mQueryTime;
    }

    public void addWeatherPeriod(WeatherPeriod wp) {
        mWeatherPeriods.add(wp);
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        this.mCityId = cityId;
    }


}
