package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.graphics.view.StateView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StateListener extends UnicastRemoteObject implements IStateObserver{

    private final transient StateView stateView;

    public StateListener(StateView stateView) throws RemoteException {
        this.stateView = stateView;
    }

    public StateView getStateView() {
        return stateView;
    }

    @Override
    public void onSetupGame() throws IOException {

    }

    @Override
    public void onSetupPlayer() throws IOException {

    }

    @Override
    public void onRoundStart(String message) throws IOException {

    }

    @Override
    public void onTurnState(String message) throws IOException {

    }

    @Override
    public void onRoundEnd(String message) throws IOException {

    }

    @Override
    public void onEndGame(String roundUser) throws IOException {

    }

    @Override
    public void onSkipTurnState(String message) throws IOException {

    }

    @Override
    public void onPlaceDiceState(String turnUser) throws IOException {

    }

    @Override
    public void onUseCardState(String turnUser) throws IOException {

    }

    @Override
    public void onEndTurnState(String turnUser) throws IOException {

    }

    @Override
    public void onVictoryPointsCalculated(String victoryPoints) throws IOException {

    }

    @Override
    public void onResultGame(String winner) throws IOException {

    }
}
