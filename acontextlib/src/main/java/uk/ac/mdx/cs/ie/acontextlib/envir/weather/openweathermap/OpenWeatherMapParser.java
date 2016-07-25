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

package uk.ac.mdx.cs.ie.acontextlib.envir.weather.openweathermap;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

import uk.ac.mdx.cs.ie.acontextlib.ContextException;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Cloudiness;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Humidity;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Precipitation;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Temperature;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Weather;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.WeatherPeriod;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Wind;

/**
 * The JSON parser which parses all OpenWeatherMap.org data into our data structures
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class OpenWeatherMapParser {

    private JSONObject mJson;

    private static final String LOG_TAG = "OpenWeatherMapParser";

    private Weather mWeather;
    private boolean empty;

    public OpenWeatherMapParser(JSONObject json, Weather weather) {
        mJson = json;
        mWeather = weather;
    }

    public Weather parseData() {

        return mWeather;
    }


    public Weather parseCurrentWeather() throws ContextException {
        try {
            int code = mJson.getInt("cod");
            if (code != 200) {
                empty = true;
                return null;
            }
            parseCityId();
            parseLocation();
            parseTime();
            parsePeriod();
            return mWeather;
        } catch (JSONException e) {
            throw new ContextException("Error in parsing weather", e);
        }
    }

    private void parseCityId() throws ContextException {
        try {
            mWeather.setCityId(mJson.getInt("id"));
        } catch (JSONException e) {
            throw new ContextException("Error in parsing city id", e);
        }
    }

    private void parseLocation() {
        try {
            mWeather.setLocation(mJson.getString("name"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void parseTime() {
        try {
            long time = mJson.getLong("dt");
            mWeather.setTime(new Date(time * 1000));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void parsePeriod() {
        WeatherPeriod period = new WeatherPeriod();
        period.setDescription("desc");
        period.setTemperature(parseTemperature());
        period.setCloudiness(parseCloudiness());
        period.setPrecipitation(parsePrecipitation());
        period.setWind(parseWind());
        period.setHumidity(parseHumidity());

        mWeather.addWeatherPeriod(period);
    }


    private Humidity parseHumidity() {
        Humidity humidity = new Humidity();
        try {
            double humid = mJson.getJSONObject("main").getDouble("humidity");
            humidity.setValue((int) humid);
            humidity.setDescription(String.format("Humidity: %d%%", (int) humid));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return humidity;
    }

    private Wind parseWind() {
        Wind wind = new Wind(Wind.SPEEDUNIT_MPS);
        try {
            double speed = mJson.getJSONObject("wind").getDouble("speed");
            double degs = mJson.getJSONObject("wind").getDouble("deg");
            wind.setSpeed((int) speed);
            wind.setDirection((int) degs, true);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return wind;
    }

    private Precipitation parsePrecipitation() {
        Precipitation rain = new Precipitation(Precipitation.UNIT_MM);
        try {
            JSONObject rainObject = mJson.optJSONObject("rain");

            if (rainObject != null) {
                Iterator<String> keys = rainObject.keys();

                while (keys.hasNext()) {
                    String strHours = keys.next();
                    float value = (float) rainObject.getDouble(strHours);
                    strHours = strHours.substring(0, 1);
                    int hours = Integer.valueOf(strHours);

                    rain.setHours(hours);
                    rain.setValue(value);

                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return rain;
    }


    private Cloudiness parseCloudiness() {
        Cloudiness cloud = new Cloudiness(Cloudiness.UNIT_PERCENT);
        try {
            double clpercent = mJson.getDouble("cloud");
            cloud.setValue((int) clpercent);
        } catch (Exception e) {

        }
        return cloud;
    }


    private Temperature parseTemperature() {
        Temperature temp = new Temperature(Temperature.UNIT_K);
        try {
            double current = mJson.getJSONObject("main").getDouble("temp");
            double high = mJson.getJSONObject("main").getDouble("temp_max");
            double low = mJson.getJSONObject("main").getDouble("temp_min");
            temp.setCurrentValue((int) current);
            temp.setHighValue((int) high);
            temp.setLowValue((int) low);
        } catch (Exception e) {

        }

        return temp;
    }


}
