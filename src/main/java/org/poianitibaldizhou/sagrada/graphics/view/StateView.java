package org.poianitibaldizhou.sagrada.graphics.view;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;

import java.io.IOException;

public class StateView implements IStateObserver{

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
