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
public class UserMoodContext extends PullObserver {

    public UserMoodContext(Context c) {
        super(c, 1800000, "UserMoodContext");
    }

    public enum Mood {HAPPY, SAD, UNKNOWN}

    private Mood mCurrentMood;

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

    public void checkContext(Mood mood) {
        if (mCurrentMood != mood) {
            mCurrentMood = mood;
            sendToContextReceivers("user.mood", getMoodString(mCurrentMood));
        }
    }

    public void updateMood(Mood mood) {
        checkContext(mood);
    }

    public String getMoodString(Mood mood) {
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
        sendToContextReceivers(UIEvent.MOOD_REQUEST);
    }

}
