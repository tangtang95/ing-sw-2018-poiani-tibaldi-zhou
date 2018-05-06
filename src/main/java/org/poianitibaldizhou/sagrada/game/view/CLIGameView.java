package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.cli.IScreen;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;

public class CLIGameView implements IGameView, IToolCardObserver, IScreen {

    private Player player;
    private final transient NetworkManager networkManager;
    private final transient ScreenManager screenManager;
    private Game game;
    private ToolCard toolCard;

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

    /**
     * Players gets notified that player needs to choose a dice.
     * If this is the player's CLI he chooses the dice and sends it back, otherwise, the other players
     * are just notified that player's is choosing a dice.
     *
     * @param player player that needs to choose the dice
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedDice(Player player) throws RemoteException {
        if(this.player.equals(player)) {
            // TODO implements choose dice
            System.out.println("===> Choose a dice: ");
            networkManager.getGameController().setDice(new Dice(1, Color.BLUE), game, toolCard);
        } else {
            System.out.println("===> Player "+ player + "is choosing a dice for using toolcard");
        }
    }

    @Override
    public void ack(String ack) throws RemoteException {
        System.out.println("===>" + ack);
    }

    @Override
    public void err(String err) throws RemoteException {
        System.out.println("===>" + err);
    }
}
