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

import java.util.Date;

/**
 * This class holds information about weather for a particular period of the day.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class WeatherPeriod {


    private Date mWeatherPeriodFrom;
    private Date mWeatherPeriodTo;
    private String mWeatherPeriodDesc;
    private Temperature mWeatherPeriodTemperature;
    private Cloudiness mWeatherPeriodCloud;
    private Precipitation mWeatherPeriodPrecip;
    private Wind mWeatherPeriodWind;
    private Humidity mWeatherPeriodHumidity;


    public String getDescription() {
        return mWeatherPeriodDesc;
    }

    public void setDescription(String desc) {
        mWeatherPeriodDesc = desc;
    }

    public Temperature getTemperature() {
        return mWeatherPeriodTemperature;
    }

    public void setTemperature(Temperature temp) {
        mWeatherPeriodTemperature = temp;
    }

    public Cloudiness getCloudiness() {
        return mWeatherPeriodCloud;
    }

    public void setCloudiness(Cloudiness cloud) {
        mWeatherPeriodCloud = cloud;
    }

    public Precipitation getPrecipitation() {
        return mWeatherPeriodPrecip;
    }

    public void setPrecipitation(Precipitation precip) {
        mWeatherPeriodPrecip = precip;
    }

    public Date getFromDate() {
        return mWeatherPeriodFrom;
    }

    public void setFromDate(Date from) {
        mWeatherPeriodFrom = from;
    }

    public Date getToDate() {
        return mWeatherPeriodTo;
    }

    public void setToDate(Date to) {
        mWeatherPeriodTo = to;
    }

    public void setWind(Wind w) {
        mWeatherPeriodWind = w;
    }

    public Wind getWind() {
        return mWeatherPeriodWind;
    }

    public void setHumidity(Humidity humid) {
        mWeatherPeriodHumidity = humid;
    }

    public Humidity getHumidity() {
        return mWeatherPeriodHumidity;
    }

}
