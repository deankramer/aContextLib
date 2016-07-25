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
 * This class holds information about cloudiness
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class Cloudiness {

    public static final int UNIT_PERCENT = 1;
    public static final int UNIT_OKTA = 2;

    private int mCloudUnit;
    private int mCloudValue = Integer.MIN_VALUE;
    private String mCloudDesc;

    public Cloudiness(int cloudUnit) {
        mCloudUnit = cloudUnit;
    }

    public Cloudiness(int cloudUnit, int value) {
        mCloudUnit = cloudUnit;
        mCloudValue = value;
    }

    public void setValue(int value) {
        mCloudValue = value;
    }

    public void setValue(int value, int cloudUnit) {
        if (value == Integer.MIN_VALUE) {
            mCloudValue = value;
            return;
        }

        mCloudValue = convertCloudiness(value, cloudUnit, mCloudUnit);
    }

    public int getValue() {
        return mCloudValue;
    }

    public int getCloudinessUnit() {
        return mCloudUnit;
    }

    public static int convertCloudiness(int value, int currentCloudUnit, int desiredCloudUnit) {

        switch (currentCloudUnit) {
            case UNIT_PERCENT:
                switch (desiredCloudUnit) {
                    case UNIT_PERCENT:
                        return value;
                    case UNIT_OKTA:
                        return (int) Math.round(8.0 / 100 * value);
                }

            case UNIT_OKTA:
                switch (desiredCloudUnit) {
                    case UNIT_PERCENT:
                        return (int) Math.round(100 / 8.0 * value);
                    case UNIT_OKTA:
                        return value;
                }
        }

        return value;
    }

    public String getDescription() {
        return mCloudDesc;
    }

    public void setDescription(String desc) {
        mCloudDesc = desc;
    }

}
