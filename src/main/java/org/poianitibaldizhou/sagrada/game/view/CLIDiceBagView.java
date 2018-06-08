package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLIDiceBagView extends UnicastRemoteObject implements IDrawableCollectionObserver {

    private final transient CLIStateView cliStateView;

    public CLIDiceBagView(CLIStateView cliStateView) throws RemoteException {
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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLIDiceBagView)) return false;
        if (!super.equals(o)) return false;
        CLIDiceBagView that = (CLIDiceBagView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}