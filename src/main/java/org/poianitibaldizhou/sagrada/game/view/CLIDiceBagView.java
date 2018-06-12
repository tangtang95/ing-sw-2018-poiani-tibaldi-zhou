package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

/**
 * This class implement the IDrawableCollectionObserver and it takes care
 * of printing the notify of diceBag on-screen
 */
public class CLIDiceBagView extends UnicastRemoteObject implements IDrawableCollectionObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
      */
    CLIDiceBagView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(String elem) {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(String elemList) {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementDraw(String elem) {
        /* NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStateView is the same.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLIDiceBagView)) return false;
        if (!super.equals(o)) return false;
        CLIDiceBagView that = (CLIDiceBagView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}