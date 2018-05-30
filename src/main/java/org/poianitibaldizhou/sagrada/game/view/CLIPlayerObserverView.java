package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CLIPlayerObserverView extends UnicastRemoteObject implements IPlayerObserver {

    private CLIGameView cliGameView;

    CLIPlayerObserverView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value) {
        //User user = cliGameView.getCurrentUser();
        //String message = user.getName() + " has spent " + value + "token";
        BuildGraphic buildGraphic = new BuildGraphic();
        //PrinterManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.STANDARD);
    }

    @Override
    public void onSetOutcome(Outcome outcome){
        PrinterManager.consolePrint(outcome.name(),Level.INFORMATION);
    }
}
