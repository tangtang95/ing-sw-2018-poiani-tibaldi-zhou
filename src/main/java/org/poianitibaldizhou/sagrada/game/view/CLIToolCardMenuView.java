package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public class CLIToolCardMenuView extends CLIMenuView implements IToolCardExecutorObserver, IToolCardObserver {
    private Player player;
    private String gameName;
    private String toolCardName;

    public CLIToolCardMenuView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);
    }

    @Override
    public void notifyNeedDice(List<Dice> diceList) throws RemoteException {

    }

    @Override
    public void notifyNeedNewValue() throws RemoteException {

    }

    @Override
    public void notifyNeedColor(Set<Color> colors) throws RemoteException {

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

    @Override
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws RemoteException {

    }

    @Override
    public void notifyNeedPosition() throws RemoteException {

    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException {

    }

    @Override
    public void notifyRepeatAction() {
        
    }

    @Override
    public void notifyCommandInterrupted(CommandFlow error) {

    }

    @Override
    public void onTokenChange(int tokens) {

    }

    @Override
    public void onCardDestroy() {

    }
}
