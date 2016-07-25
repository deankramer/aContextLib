package uk.ac.mdx.cs.ie.acontextlib;

/**
 * A Context Reasoner helper for context receivers.
 * Gives access to Context Manager and ReasonerManager.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public abstract class ContextReceiver implements IContextReceiver {

    private IContextManager mContextManager;
    private IReasonerManager mReasonerManager;

    public ContextReceiver() {

    }

    public ContextReceiver(IContextManager contextManager) {
        mContextManager = contextManager;
    }

    public ContextReceiver(IReasonerManager reasonerManager) {
        mReasonerManager = reasonerManager;
    }

    public ContextReceiver(IContextManager contextManager, IReasonerManager reasonerManager) {
        mContextManager = contextManager;
        mReasonerManager = reasonerManager;
    }

    public IContextManager getContextManager() {
        return mContextManager;
    }

    public IReasonerManager getReasonerManager() {
        return mReasonerManager;
    }
}
