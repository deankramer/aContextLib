package uk.ac.mdx.cs.ie.acontextlib;

import java.util.HashMap;
import java.util.Map;

/**
 * A Class designed to deal with multiple Context Receivers used by the Context Reasoner.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class ContextReceivers implements IContextReceiver {

    private Map<String, IContextReceiver> mReceiverMap = new HashMap<>();
    private ContextReceiver[] mReceivers;

    public void add(String appkey, IContextReceiver receiver) {
        mReceiverMap.put(appkey, receiver);
        mReceivers = (ContextReceiver[]) mReceiverMap.values().toArray();
    }

    public IContextReceiver remove(String appkey) {

        IContextReceiver result = mReceiverMap.remove(appkey);
        mReceivers = (ContextReceiver[]) mReceiverMap.values().toArray();

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

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, double value) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, boolean value) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, String value) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValue(String name, Object value) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValue(name, value);
        }
    }

    @Override
    public void newContextValues(Map<String, String> values) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newContextValues(values);
        }
    }

    @Override
    public void newUIEvent(int event) {

        for (ContextReceiver receiver : mReceivers) {
            receiver.newUIEvent(event);
        }
    }
}
