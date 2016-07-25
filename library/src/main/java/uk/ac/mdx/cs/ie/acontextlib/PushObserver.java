package uk.ac.mdx.cs.ie.acontextlib;

import android.content.Context;

/**
 * Created by dkram on 20/07/2016.
 */
public abstract class PushObserver extends ContextObserver {

    public PushObserver(Context context) {
        super(context);
    }

    public PushObserver(Context context, String name) {
        super(context, name);
    }

    public abstract void checkContext(Object data);

}

