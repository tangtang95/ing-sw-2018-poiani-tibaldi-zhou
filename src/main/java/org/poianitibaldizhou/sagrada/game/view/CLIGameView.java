package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;

public class CLIGameView implements IGameRemoteViewController, IToolCardObserver {

    public void run() {
        System.out.println("Welcome to the game");
    }

    @Override
    public void onTokenChange(int tokens) {

    }

    @Override
    public void onCardDestroy() {

    }

    @Override
    public void notifyNeedDice() {

    }
}
