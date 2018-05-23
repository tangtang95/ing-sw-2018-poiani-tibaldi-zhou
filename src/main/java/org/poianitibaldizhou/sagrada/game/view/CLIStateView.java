package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.observers.IStateObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.Map;

public class CLIStateView implements IStateObserver {

    private final CLIGameView cliGameView;
    private final BufferManager bufferManager;

    public CLIStateView(CLIGameView cliGameView, BufferManager bufferManager) {
        this.cliGameView = cliGameView;
        this.bufferManager = bufferManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() throws RemoteException {
        bufferManager.consolePrint("Match is getting set...\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() throws RemoteException {
        bufferManager.consolePrint("Players are getting set...\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(Map<String, Integer> victoryPoints) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner) throws RemoteException {

    }
}