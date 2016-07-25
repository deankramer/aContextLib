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

/**
 * This class holds information about temperature
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class Temperature {

    private int mTemperatureUnit;
    private int mCurrentTemp = Integer.MIN_VALUE;
    private int mHighTemp = Integer.MIN_VALUE;
    private int mLowTemp = Integer.MIN_VALUE;

    public static final int UNIT_C = 1;
    public static final int UNIT_F = 2;
    public static final int UNIT_K = 3;

    public Temperature(int tempUnit) {
        mTemperatureUnit = tempUnit;
    }

    public Temperature(int tempUnit, int currentTemp) {
        mTemperatureUnit = tempUnit;
        mCurrentTemp = currentTemp;
    }

    public Temperature(int tempUnit, int currentTemp, int highTemp, int lowTemp) {
        mTemperatureUnit = tempUnit;
        mCurrentTemp = currentTemp;
        mHighTemp = highTemp;
        mLowTemp = lowTemp;
    }

    public int getTemperatureUnit() {
        return mTemperatureUnit;
    }

    public void setHighValue(int value) {
        mHighTemp = value;
    }

    public void setLowValue(int value) {
        mLowTemp = value;
    }

    public void setCurrentValue(int value) {
        mCurrentTemp = value;
    }

    public void setTemperatureUnit(int newUnit) {
        if (newUnit == mTemperatureUnit) {
            return;
        }

        if (mCurrentTemp != Integer.MIN_VALUE) {
            mCurrentTemp = convertTemperature(mCurrentTemp, mTemperatureUnit, newUnit);
        }

        if (mHighTemp != Integer.MIN_VALUE) {
            mHighTemp = convertTemperature(mHighTemp, mTemperatureUnit, newUnit);
        }

        if (mLowTemp != Integer.MIN_VALUE) {
            mLowTemp = convertTemperature(mLowTemp, mTemperatureUnit, newUnit);
        }

        mTemperatureUnit = newUnit;
    }

    public int getCurrentValue() {
        return mCurrentTemp;
    }

    public void setCurrentValue(int value, int tempUnit) {
        mCurrentTemp = convertTemperature(value, tempUnit, mTemperatureUnit);
    }

    public int getHighValue() {
        return mHighTemp;
    }

    public void setHighValue(int value, int tempUnit) {
        mHighTemp = convertTemperature(value, tempUnit, mTemperatureUnit);
    }

    public int getLowValue() {
        return mLowTemp;
    }

    public void setLowValue(int value, int tempUnit) {
        mLowTemp = convertTemperature(value, tempUnit, mTemperatureUnit);
    }

    public static int convertTemperature(int value, int currentTempUnit, int desiredTempUnit) {

        switch (currentTempUnit) {
            case UNIT_C:
                switch (desiredTempUnit) {
                    case UNIT_C:
                        return value;
                    case UNIT_F:
                        return Math.round(value * 9f / 5f + 32);
                    case UNIT_K:
                        return Math.round(value + 273.15f);
                }

            case UNIT_F:
                switch (desiredTempUnit) {
                    case UNIT_C:
                        return Math.round((value - 32) * 5f / 9f);
                    case UNIT_F:
                        return value;
                    case UNIT_K:
                        return Math.round((value - 32) * 5f / 9f + 273.15f);
                }

            case UNIT_K:
                switch (desiredTempUnit) {
                    case UNIT_C:
                        return Math.round(value - 273.15f);
                    case UNIT_F:
                        return Math.round((value - 273.15f) * 9f / 5f + 32);
                    case UNIT_K:
                        return value;
                }
        }

        return value;
    }


}
