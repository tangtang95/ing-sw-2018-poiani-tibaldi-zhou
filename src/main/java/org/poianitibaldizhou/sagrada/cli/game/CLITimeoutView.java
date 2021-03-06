package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ITimeOutObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * This class implement the ITimeOutObserver and it takes care
 * of printing the notify of the timeOut on-screen.
 */
public class CLITimeoutView extends UnicastRemoteObject implements ITimeOutObserver {

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    private final transient CLIStateView cliStateView;

    /**
     * Constructor.
     *
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLITimeoutView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.clientGetMessage = new ClientGetMessage();
        this.cliStateView = cliStateView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimeOut(String message) throws IOException {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        String username = clientGetMessage.getUserWrapper(message).getUsername();
        if(username.equals(cliStateView.getMyUser().getUsername())) {
            PrinterManager.consolePrint(ClientMessage.TIMEOUT, Level.INFORMATION);
        } else {
            PrinterManager.consolePrint(String.format(ClientMessage.USER_HAS_TIMEOUT, username), Level.INFORMATION);
        }
        consoleListener.stopReadNumber();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLITimeoutView is the same.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof CLITimeoutView;
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
