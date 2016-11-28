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

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;

import uk.ac.mdx.cs.ie.acontextlib.ContextException;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.Weather;
import uk.ac.mdx.cs.ie.acontextlib.envir.weather.source.WeatherSource;
import uk.ac.mdx.cs.ie.acontextlib.utility.InternetSource;

/**
 * Class to handle downloading of weather data before getting it parsed.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class OpenWeatherMapSource extends InternetSource implements WeatherSource {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String FORECAST_URL = BASE_URL + "forecast?";
    private static final String DAILY_URL = BASE_URL + "forecast/daily?";
    private static final String CURRENT_URL = BASE_URL + "weather?";
    private Context mContext;
    private String API_KEY;

    public OpenWeatherMapSource(Context context) {
        mContext = context;
        Bundle metadata = null;

        try {
            metadata = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        API_KEY = metadata.getString("openWeatherMap_ApiKey", "");
    }

    public void setApiKey(String key) {
        API_KEY = key;
    }

    @Override
    public Weather query(Location place, int numOfDays) throws ContextException {
        if (place == null) {
            throw new ContextException("Null location object");
        }

        JSONObject json;
        Weather weather = new Weather();

        if (numOfDays == 0) {
            json = queryCurrentWeather(place);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseCurrentWeather();
        } else if (numOfDays == 1) {
            json = queryWeatherForecast(place, 8);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseData();
        } else {
            json = queryDailyForecast(place, numOfDays);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseData();
        }
    }

    @Override
    public Weather query(String place, int numOfDays) throws ContextException {
        if (place == null) {
            throw new ContextException("Null location object");
        }

        place = sanitizeString(place);

        JSONObject json;
        Weather weather = new Weather();

        if (numOfDays == 0) {
            json = queryCurrentWeather(place);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseCurrentWeather();
        } else if (numOfDays == 1) {
            json = queryWeatherForecast(place, 8);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseData();
        } else {
            json = queryDailyForecast(place, numOfDays);
            OpenWeatherMapParser parser = new OpenWeatherMapParser(json, weather);
            return parser.parseData();
        }

    }

    private static String sanitizeString(String place) {

        String[] placeSections = place.split(",");

        StringBuilder sanitizedString = new StringBuilder();

        for (String section : placeSections) {
            if (sanitizedString.length() > 0) {
                sanitizedString.append(",");
            }

            section = section.trim();
            section = section.replaceAll("\\s", "%20");

            sanitizedString.append(section);
        }

        return sanitizedString.toString();
    }

    public JSONObject queryCurrentWeather(String place) throws ContextException {
        String url = CURRENT_URL + "q=" + place;
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }

    }

    public JSONObject queryCurrentWeather(Location place) throws ContextException {
        String url = CURRENT_URL + "lat=" + place.getLatitude() + "&lon=" + place.getLongitude();
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }
    }

    public JSONObject queryDailyForecast(Location place, int numOfDays) throws ContextException {
        String url = DAILY_URL + "lat=" + place.getLatitude() + "&lon=" + place.getLongitude();
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }
    }

    public JSONObject queryWeatherForecast(Location place, int numOfPeriods) throws ContextException {
        String url = FORECAST_URL + "lat=" + place.getLongitude() + "&lon" + place.getLongitude();
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }
    }

    public JSONObject queryDailyForecast(String place, int numOfDays) throws ContextException {
        String url = DAILY_URL + "q=" + place;
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }
    }

    public JSONObject queryWeatherForecast(String place, int numOfPeriods) throws ContextException {
        String url = FORECAST_URL + "q=" + place;
        JSONTokener parser = new JSONTokener(readToString(url));
        try {
            return (JSONObject) parser.nextValue();
        } catch (JSONException e) {
            throw new ContextException("Error parsing weather", e);
        }
    }


    @Override
    protected void prepareRequest(HttpURLConnection request) {
        if (!API_KEY.isEmpty()) {
            request.addRequestProperty("X-API-Key", API_KEY);
        }
    }

}
