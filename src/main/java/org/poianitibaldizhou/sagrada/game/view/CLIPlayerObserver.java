package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.game.model.observers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;

public class CLIPlayerObserver implements IPlayerObserver {

    private transient BufferManager bufferManager;
    private transient CLIGameView cliGameView;

    public CLIPlayerObserver(CLIGameView cliGameView, BufferManager bufferManager) {
        this.bufferManager = bufferManager;
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(int value) {
        User user = cliGameView.getCurrentUser();
        String message = user.getName() + " has spent " + value + "token";
        BuildGraphic buildGraphic = new BuildGraphic();
        bufferManager.consolePrint(buildGraphic.buildMessage(message).toString(), Level.LOW);
    }

    @Override
    public void onSetOutcome(Outcome outcome) throws RemoteException {

    }
}
