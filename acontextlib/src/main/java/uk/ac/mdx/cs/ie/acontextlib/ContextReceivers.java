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

    @Override
    public void newUIEvent(int event) {

        for (IContextReceiver receiver : mReceivers) {
            receiver.newUIEvent(event);
        }
    }
}
