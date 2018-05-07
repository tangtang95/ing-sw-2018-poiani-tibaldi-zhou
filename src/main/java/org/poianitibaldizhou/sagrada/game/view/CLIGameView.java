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
import java.util.List;

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
     * Players gets notified that player needs to choose a dice from diceList.
     * If this is the player's CLI he chooses the dice and sends it back, otherwise, the other players
     * are just notified that player's is choosing a dice.
     *
     * @param player player that needs to choose the dice
     * @param diceList player needs to choose a dice among this list
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedDice(Player player, List<Dice> diceList) throws RemoteException {
        if(this.player.equals(player)) {
            // TODO implements choose dice
            System.out.println("===> Choose a dice: ");
            networkManager.getGameController().setDice(new Dice(1, Color.BLUE), game, toolCard);
        } else {
            System.out.println("===> Player "+ player + "is choosing a dice for using toolcard");
        }
    }

    /**
     * Players get notified that player needs to choose a new value for a certain dice.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @param player player that needs to choose a new value
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedNewValue(Player player) throws RemoteException {
        if(this.player.equals(player)) {
            // TODO read value
            System.out.println("===> Choose a value for the dice: ");
            int value = 5;
            networkManager.getGameController().setNewValue(value, game, toolCard);
        }
    }

    /**
     * Players get notified that player needs to choose a color from the list of colors
     * present in the round track.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @param player player that needs to choose a color
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedColor(Player player) throws RemoteException {
        if(this.player.equals(player)) {
            // TODO choose color from roundtrack
            System.out.println("===> Choose a color from a dice present in the round track: ");
            Color color = Color.YELLOW;
            networkManager.getGameController().setColor(color, game,toolCard);
        }
    }

    /**
     * Players get notified that player needs to modify a value of a certain dice by +/- value.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     * Note that if the modified value can't exceed Dice.MAX_VALUE nor DICE_MIN_VALUE, so
     * if value = 1 and diceValue=6, the modified can't be 1, for instance.
     *
     * @param diceValue player that needs to choose a color
     * @param value delta used to modify diceValue
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException {
        if(this.player.equals(player)) {
            // TODO choose delta
            System.out.println("===> Modify " + diceValue + "of +/-"+ value);
            int modifiedValue = 5;
            networkManager.getGameController().setNewValue(modifiedValue, game, toolCard);
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
