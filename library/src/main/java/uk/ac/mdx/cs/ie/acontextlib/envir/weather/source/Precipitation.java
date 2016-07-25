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
 * This class holds information about precipitation
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class Precipitation {

    private int mPeriodHours = 0;
    private int mPrecipitationUnit;
    private float mPrecipitationValue = 0;

    public static final int UNIT_MM = 1;
    public static final int UNIT_IN = 2;

    public Precipitation(int unit) {
        mPrecipitationUnit = unit;
    }

    public int getHours() {
        return mPeriodHours;
    }

    public void setHours(int hours) {
        mPeriodHours = hours;
    }

    public void setValue(float value) {
        mPrecipitationValue = value;
    }

    public void setValue(float value, int valueUnit) {
        mPrecipitationValue = convertPrecipitation(value, valueUnit, mPrecipitationUnit);
    }

    public void setPrecipitationUnit(int newUnit) {
        if (newUnit == mPrecipitationUnit) {
            return;
        }

        if (mPrecipitationValue != 0) {
            mPrecipitationValue =
                    convertPrecipitation(mPrecipitationValue, mPrecipitationUnit, newUnit);
        }

        mPrecipitationUnit = newUnit;
    }

    public float getValue() {
        return mPrecipitationValue;
    }

    public static float convertPrecipitation(float value, int currentPrecipUnit, int desiredPrecipUnit) {

        if (value == 0) {
            return value;
        }

        switch (currentPrecipUnit) {
            case UNIT_MM:
                switch (desiredPrecipUnit) {
                    case UNIT_MM:
                        return value;
                    case UNIT_IN:
                        return (float) (value * 0.03937);
                }

            case UNIT_IN:
                switch (desiredPrecipUnit) {
                    case UNIT_MM:
                        return (float) (value * 25.4);
                    case UNIT_IN:
                        return value;
                }
        }

        return value;
    }

}
