package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CLIPlayerObserverView extends UnicastRemoteObject implements IPlayerObserver {

    private CLIGameView cliGameView;

    public CLIPlayerObserverView(CLIGameView cliGameView) throws RemoteException {
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

    @Override
    public void onSetOutcome(String outcome) throws IOException {
        String message = "Your outcome is: " + cliGameView.getClientGetMessage().getOutcome(outcome);
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }
}
