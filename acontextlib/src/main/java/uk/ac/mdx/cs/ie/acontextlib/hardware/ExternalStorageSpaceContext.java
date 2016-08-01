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

package uk.ac.mdx.cs.ie.acontextlib.hardware;

import android.content.Context;
import android.os.Environment;

import uk.ac.mdx.cs.ie.acontextlib.PullObserver;

/**
 * Handles Storage Related Contexts and returns values in terms of MB
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class ExternalStorageSpaceContext extends PullObserver {

    private final static long SIZE_KB = 1024L;
    private final static long SIZE_MB = SIZE_KB * SIZE_KB;
    private long mCurrentSpace;
    public static final String RECEIVER_EXTERNSTORAGESPACE = "sensor.external_storage_remaining";

    public ExternalStorageSpaceContext(Context c) {
        super(c, 120000, "StorageContext");
    }

    @Override
    public boolean start() {
        checkContext();
        return super.start();
    }

    @Override
    public void checkContext() {
        long v = Environment.getExternalStorageDirectory().getUsableSpace();
        if (mCurrentSpace != v) {
            sendToContextReceivers(RECEIVER_EXTERNSTORAGESPACE, v / SIZE_MB);
            mCurrentSpace = v;
        }

    }

}
