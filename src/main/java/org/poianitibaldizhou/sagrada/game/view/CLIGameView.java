package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.cli.IScreen;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.List;

public class CLIGameView implements IGameView, IToolCardExecutorObserver, IToolCardObserver, IScreen {

    private Player player;
    private final transient NetworkManager networkManager;
    private final transient ScreenManager screenManager;
    private String gameName;
    private String toolCardName;

    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager) {
        this.networkManager = networkManager;
        this.screenManager = screenManager;
    }

    public void run() {
        System.out.println("Welcome to the gameName");
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
     * @param diceList player needs to choose a dice among this list
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedDice(List<Dice> diceList) throws RemoteException {
        // TODO implements choose dice
        System.out.println("===> Choose a dice: ");
        networkManager.getGameController().setDice(new Dice(1, Color.BLUE), gameName, toolCardName);
    }

    /**
     * Players get notified that player needs to choose a new value for a certain dice.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedNewValue() throws RemoteException {
        // TODO read value
        System.out.println("===> Choose a value for the dice: ");
        int value = 5;
        networkManager.getGameController().setNewValue(value, gameName, toolCardName);
    }

    /**
     * Players get notified that player needs to choose a color from the list of colors
     * present in the round track.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedColor() throws RemoteException {
        if (this.player.equals(player)) {
            // TODO choose color from roundtrack
            System.out.println("===> Choose a color from a dice present in the round track: ");
            Color color = Color.YELLOW;
            networkManager.getGameController().setColor(color, gameName, toolCardName);
        }
    }

    /**
     * Players get notified that player needs to modify a value of a certain dice by +/- value.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     * Note that if the modified value can't exceed Dice.MAX_VALUE nor DICE_MIN_VALUE, so
     * if value = 1 and diceValue=6, the modified can't be 1, for instance.
     *
     * @param diceValue player that needs to choose a color
     * @param value     delta used to modify diceValue
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException {
        if (this.player.equals(player)) {
            // TODO choose delta
            System.out.println("===> Modify " + diceValue + "of +/-" + value);
            int modifiedValue = 5;
            networkManager.getGameController().setNewValue(modifiedValue, gameName, toolCardName);
        }
    }

    /**
     * Players get notified that player needs to choose a dice from a given RoundTrack.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @param roundTrack RoundTrack that contains the list of dices among which the player has to choose
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws RemoteException {
        if (this.player.equals(player)) {
            // TODO implements dice choose from roundtrack
            int round = 1;
            Dice dice = new Dice(1, Color.YELLOW);
            networkManager.getGameController().setDice(dice, gameName, toolCardName);
            networkManager.getGameController().setNewValue(round, gameName, toolCardName);
        }
    }

    /**
     * Players get notified that player needs to choose a position of the schema card.
     * If this is the player's CLI, he chooses the position and sends it back, nothing otherwise.
     *
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedPosition() throws RemoteException {
        if (this.player.equals(player)) {
            // TODO implements position
            Position position;
            try {
                position = new Position(1, 1);
                networkManager.getGameController().setPosition(position, gameName, toolCardName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Players get notified that player needs to choose a position from the schema card.
     * The position selected, must contain a dice of the specified color.
     * If this is the player's CLI, he chooses the value and sends it back, nothing otherwise.
     *
     * @param color  color of the dice of the selected position
     * @throws RemoteException network communication error
     */
    @Override
    public void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException {
        if (this.player.equals(player)) {
            // TODO implements position choose, the schemacard must contain a dice of that color
            Position position;
            try {
                position = new Position(1, 1);
                networkManager.getGameController().setPosition(position, gameName, toolCardName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

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
