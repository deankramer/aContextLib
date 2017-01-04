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

package uk.ac.mdx.cs.ie.acontextlib.personal;

import android.content.Context;

import uk.ac.mdx.cs.ie.acontextlib.PullObserver;
import uk.ac.mdx.cs.ie.acontextlib.UIEvent;

/**
 * Keeps track of user moods.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class UserMoodObserver extends PullObserver {

    public UserMoodObserver(Context c) {
        super(c, 1800000, "UserMoodObserver");
    }

    public static final int UNKNOWN = -1;
    public static final int HAPPY = 0;
    public static final int SAD = 1;
    public static final String RECEIVER_MOOD = "user.mood";
    public static final String RECEIVER_MOOD_REQUEST = "user.mood_request";


    private int mCurrentMood;

    @Override
    public boolean start() {
        super.start();
        return true;
    }

    @Override
    public boolean pause() {
        super.pause();
        return true;
    }

    @Override
    public boolean resume() {
        super.resume();
        return true;
    }

    @Override
    public boolean stop() {
        super.stop();
        return true;
    }

    public void checkContext(int mood) {
        if (mCurrentMood != mood) {
            mCurrentMood = mood;
            sendToContextReceivers(RECEIVER_MOOD, getMoodString(mCurrentMood));
        }
    }

    public void updateMood(int mood) {
        checkContext(mood);
    }

    public String getMoodString(int mood) {
        switch (mood) {
            case HAPPY:
                return "happy";
            case SAD:
                return "sad";
            case UNKNOWN:
                return "unknown";
            default:
                return "unknown";
        }
    }

    public void checkContext() {
        sendToContextReceivers(RECEIVER_MOOD_REQUEST, UIEvent.MOOD_REQUEST);
    }

}
