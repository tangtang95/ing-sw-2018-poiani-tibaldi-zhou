package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

/**
 * This class implement the IPlayerObserver and it takes care
 * of printing the notify of player on-screen.
 */
public class CLIPlayerView extends UnicastRemoteObject implements IPlayerObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    private final String username;


    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @param username player's username
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIPlayerView(CLIStateView cliStateView, String username) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        String message = String.format(ClientMessage.TOKEN_SPENT,
                cliStateView.getCurrentUser().getUsername(),clientGetMessage.getValue(value));
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(String outcome) throws IOException {
        String message = ClientMessage.YOUR_OUTCOME + clientGetMessage.getOutcome(outcome);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.INFORMATION);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStateView is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIPlayerView)) return false;
        if (!super.equals(o)) return false;
        CLIPlayerView that = (CLIPlayerView) o;
        return Objects.equals(cliStateView, that.cliStateView);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }

}
