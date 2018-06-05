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

    private final transient CLIStateScreen cliStateScreen;
    private final transient ClientGetMessage clientGetMessage;

    public CLIPlayerView(CLIStateScreen cliStateScreen) throws RemoteException {
        super();
        this.cliStateScreen = cliStateScreen;
        this.clientGetMessage = cliStateScreen.getClientGetMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        String message = cliStateScreen.getCurrentUser().getUsername() + " has spent " +
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
        return Objects.equals(cliStateScreen, that.cliStateScreen) &&
                Objects.equals(clientGetMessage, that.clientGetMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliStateScreen, clientGetMessage);
    }

}
