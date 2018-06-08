package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

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


    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIPlayerView(CLIStateView cliStateView) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        String message = cliStateView.getCurrentUser().getUsername() + " has spent " +
                clientGetMessage.getValue(value) + " token";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(String outcome) throws IOException {
        String message = "Your outcome is: " + clientGetMessage.getOutcome(outcome);
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
        return this.getClass().getSimpleName().hashCode();
    }

}
