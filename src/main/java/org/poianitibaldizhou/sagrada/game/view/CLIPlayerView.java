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

public class CLIPlayerView extends UnicastRemoteObject implements IPlayerObserver {

    private final transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;
    private final String username;

    public CLIPlayerView(CLIStateView cliStateView, String username) throws RemoteException {
        super();
        this.cliStateView = cliStateView;
        this.username = username;
        this.clientGetMessage = cliStateView.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        String message = cliStateView.getCurrentUser().getUsername() + " has spent " +
                clientGetMessage.getValue(value) + "token";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(String outcome) throws IOException {
        String message = "Your outcome is: " + clientGetMessage.getOutcome(outcome);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIPlayerView)) return false;
        if (!super.equals(o)) return false;
        CLIPlayerView that = (CLIPlayerView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }

}
