package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.lobby.view.IScreen;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

public class CLIGameView implements IGameView, IToolCardObserver, IScreen {

    private final transient NetworkManager networkManager;
    private final transient ScreenManager screenManager;

    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager){
        this.networkManager = networkManager;
        this.screenManager = screenManager;
    }

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
