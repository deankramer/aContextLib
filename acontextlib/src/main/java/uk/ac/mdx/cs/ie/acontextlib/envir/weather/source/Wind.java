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
 * This class holds information about wind
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class Wind {

    private int mWindSpeedUnit;
    private int mWindDirection;
    private int mWindSpeed;
    private String mWindDesc;

    public static final int DIRECTION_N = 1;
    public static final int DIRECTION_NE = 2;
    public static final int DIRECTION_E = 3;
    public static final int DIRECTION_SE = 4;
    public static final int DIRECTION_S = 5;
    public static final int DIRECTION_SW = 6;
    public static final int DIRECTION_W = 7;
    public static final int DIRECTION_NW = 8;

    public static final int SPEEDUNIT_KPH = 9;
    public static final int SPEEDUNIT_MPH = 10;
    public static final int SPEEDUNIT_MPS = 11;
    public static final int SPEEDUNIT_KN = 12;
    public static final int SPEEDUNIT_FPS = 13;

    public Wind(int sunit) {
        mWindSpeedUnit = sunit;
    }

    public Wind(int dir, int sunit) {
        mWindDirection = dir;
        mWindSpeedUnit = sunit;
    }

    public Wind(int dir, int sunit, int speed, boolean degrees) {

        if (degrees) {
            mWindDirection = getDirection(dir);
        } else {
            mWindDirection = dir;
        }

        mWindSpeedUnit = sunit;
        mWindSpeed = convertSpeed(speed, sunit, sunit);
    }

    public int getSpeed() {
        return mWindSpeed;
    }

    public int getSpeedUnit() {
        return mWindSpeedUnit;
    }

    public void setSpeed(int speed) {
        mWindSpeed = speed;
    }

    public void setSpeed(int speed, int speedunit) {

        if (mWindSpeedUnit == speedunit) {
            mWindSpeed = speed;
        } else {
            mWindSpeed = convertSpeed(speed, speedunit, mWindSpeedUnit);
        }
    }

    public void setDirectionUnit(int dir) {
        mWindDirection = dir;
    }

    public void setDirection(int dir, boolean degrees) {

        if (degrees) {
            mWindDirection = getDirection(dir);
        } else {
            mWindDirection = dir;
        }

    }

    public int getDirection() {
        return mWindDirection;
    }

    public void setDescription(String desc) {
        mWindDesc = desc;
    }

    public String getDescription() {
        return mWindDesc;
    }


    public static int getDirection(int degrees) {

        int degreesPositive = degrees;
        if (degrees < 0) {
            degreesPositive += (-degrees / 360 + 1) * 360;
        }
        int degreesNormalized = degreesPositive % 360;
        int degreesRotated = degreesNormalized + (360 / 16 / 2);
        int zone = degreesRotated / (360 / 8);

        switch (zone) {
            case 0:
                return DIRECTION_N;
            case 1:
                return DIRECTION_NE;
            case 2:
                return DIRECTION_E;
            case 3:
                return DIRECTION_SE;
            case 4:
                return DIRECTION_S;
            case 5:
                return DIRECTION_SW;
            case 6:
                return DIRECTION_W;
            case 7:
                return DIRECTION_NW;
        }

        return DIRECTION_N;

    }

    public static int convertSpeed(int speed, int currentSpeedUnit, int desiredSpeedUnit) {

        switch (currentSpeedUnit) {

            case SPEEDUNIT_KPH:
                switch (desiredSpeedUnit) {
                    case SPEEDUNIT_KPH:
                        return speed;
                    case SPEEDUNIT_MPH:
                        return (int) Math.round(speed * 0.62137);
                    case SPEEDUNIT_MPS:
                        return (int) Math.round(speed * 0.27777);
                    case SPEEDUNIT_KN:
                        return (int) Math.round(speed * 0.53995);
                    case SPEEDUNIT_FPS:
                        return (int) Math.round(speed * 0.91134);
                }

            case SPEEDUNIT_MPH:
                switch (desiredSpeedUnit) {
                    case SPEEDUNIT_KPH:
                        return (int) Math.round(speed * 1.60934);
                    case SPEEDUNIT_MPH:
                        return speed;
                    case SPEEDUNIT_MPS:
                        return (int) Math.round(speed * 0.44704);
                    case SPEEDUNIT_KN:
                        return (int) Math.round(speed * 0.86897);
                    case SPEEDUNIT_FPS:
                        return (int) Math.round(speed * 1.46666);
                }

            case SPEEDUNIT_MPS:
                switch (desiredSpeedUnit) {
                    case SPEEDUNIT_KPH:
                        return (int) Math.round(speed * 3.60000);
                    case SPEEDUNIT_MPH:
                        return (int) Math.round(speed * 2.23693);
                    case SPEEDUNIT_MPS:
                        return speed;
                    case SPEEDUNIT_KN:
                        return (int) Math.round(speed * 1.94384);
                    case SPEEDUNIT_FPS:
                        return (int) Math.round(speed * 3.28084);
                }

            case SPEEDUNIT_KN:
                switch (desiredSpeedUnit) {
                    case SPEEDUNIT_KPH:
                        return (int) Math.round(speed * 1.85200);
                    case SPEEDUNIT_MPH:
                        return (int) Math.round(speed * 1.15077);
                    case SPEEDUNIT_MPS:
                        return (int) Math.round(speed * 0.51444);
                    case SPEEDUNIT_KN:
                        return speed;
                    case SPEEDUNIT_FPS:
                        return (int) Math.round(speed * 1.68781);
                }

            case SPEEDUNIT_FPS:
                switch (desiredSpeedUnit) {
                    case SPEEDUNIT_KPH:
                        return (int) Math.round(speed * 1.09728);
                    case SPEEDUNIT_MPH:
                        return (int) Math.round(speed * 1.09728);
                    case SPEEDUNIT_MPS:
                        return (int) Math.round(speed * 0.30480);
                    case SPEEDUNIT_KN:
                        return (int) Math.round(speed * 0.59248);
                    case SPEEDUNIT_FPS:
                        return speed;
                }
        }

        return Integer.MIN_VALUE;
    }
}
