package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class CLIPlayerView extends UnicastRemoteObject implements IPlayerObserver {

    private CLIGameView cliGameView;

    public CLIPlayerView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        String message = cliGameView.getCurrentUser().getUsername() + " has spent " + cliGameView.getClientGetMessage().getValue(value) + "token";
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(String outcome) throws IOException {
        String message = "Your outcome is: " + cliGameView.getClientGetMessage().getOutcome(outcome);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliGameView);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIDraftPoolView)) return false;
        if (!super.equals(o)) return false;
        CLIPlayerView that = (CLIPlayerView) o;
        return Objects.equals(cliGameView, that.cliGameView);
    }
}
