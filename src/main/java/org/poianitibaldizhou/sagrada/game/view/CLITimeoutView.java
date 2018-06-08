package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLITimeoutView extends UnicastRemoteObject implements ITimeOutObserver {

    private transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;

    public CLITimeoutView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = new ClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimeOut(String message) throws IOException {
        String username = clientGetMessage.getTurnUserWrapper(message).getUsername();

        PrinterManager.consolePrint("User " + username + " has timed out", Level.STANDARD);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CLITimeoutView;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
