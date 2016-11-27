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

package uk.ac.mdx.cs.ie.acontextlib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A Class designed to deal with multiple Context Receivers used by the Context Reasoner/Applications.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class ContextReceivers implements IContextReceiver {

    private Map<String, IContextReceiver> mReceiverMap = new HashMap<>();
    private Collection<IContextReceiver> mReceivers;

    public void add(String appkey, IContextReceiver receiver) {

        mReceiverMap.put(appkey, receiver);
        mReceivers = mReceiverMap.values();
    }


    public IContextReceiver remove(String appkey) {

        IContextReceiver result = mReceiverMap.remove(appkey);
        mReceivers = mReceiverMap.values();
        return result;
    }

    public boolean contains(IContextReceiver receiver) {
        return mReceiverMap.containsValue(receiver);
    }

    public boolean contains(String appkey) {
        return mReceiverMap.containsKey(appkey);
    }

    @Override
    public void newContextValue(String name, long value) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, double value) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, boolean value) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, String value) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, Object value) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValues(Map<String, String> values) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newContextValues(values);
        }
    }
}
