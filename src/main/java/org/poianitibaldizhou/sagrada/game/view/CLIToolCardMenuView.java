package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.model.*;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.*;

public class CLIToolCardMenuView extends CLIMenuView implements IToolCardExecutorObserver, IToolCardObserver {
    private final transient ToolCard toolCard;
    private final String gameName;

    private static final String NUMBER_WARNING = "WARNING: Number is not correct";

    public CLIToolCardMenuView(NetworkManager networkManager, ScreenManager screenManager, ToolCard toolCards,
                               String gameName)
            throws RemoteException {
        super(networkManager, screenManager);
        this.toolCard = toolCards;
        this.gameName = gameName;

    }

    @Override
    public void notifyNeedDice(List<Dice> diceList) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder();
        String response;
        int number;

        printListDice(diceList, stringBuilder);
        do {
            response = getAnswer("Choose a dice:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < diceList.size()) {
                networkManager.getGameController().setDice(diceList.get(number - 1), gameName, toolCard.getName());
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
    }

    @Override
    public void notifyNeedNewValue() throws RemoteException {
        String response;
        int number;

        do {
            response = getAnswer("Choose a number between 1 and 6:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < 7) {
                networkManager.getGameController().setNewValue(number, gameName, toolCard.getName());
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
    }

    @Override
    public void notifyNeedColor(Set<Color> colors) throws RemoteException {
        String response;
        int number;

        bufferManager.consolePrint("Colors: ", Level.LOW);
        for (int i = 0; i < colors.size(); i++) {
            bufferManager.consolePrint("[" + i + 1 + "] " + colors.toArray()[i].toString() + "\n", Level.LOW);
        }

        do {
            response = getAnswer("Choose a color:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < 7) {
                networkManager.getGameController().setColor((Color) colors.toArray()[number - 1],
                        gameName, toolCard.getName());
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
    }

    private Set<Integer> createDeltaSetDice(int diceValue, int value) {
        Set<Integer> intSet = new HashSet<>();
        boolean enable;
        int startNumber;

        enable = diceValue == Dice.MAX_VALUE || diceValue == Dice.MIN_VALUE;

        if (value >= diceValue) {
            if (enable) {
                intSet.add(2);
                intSet.add(3);
                intSet.add(4);
                intSet.add(5);
            } else {
                for (int i = 1; i <= Dice.MAX_VALUE; i++) {
                    if (i != diceValue)
                        intSet.add(i);
                }
            }
            return intSet;
        }

        for (int i = diceValue + 1; i < diceValue + value; i++) {
            if (i < Dice.MAX_VALUE || (i == Dice.MAX_VALUE && !enable))
                intSet.add(i);
            else if (i % (Dice.MAX_VALUE + 1 )> 1 || (i % (Dice.MAX_VALUE + 1) == Dice.MIN_VALUE && !enable))
                intSet.add(i % Dice.MAX_VALUE);
        }

        startNumber = diceValue - value <= 0 ? (Dice.MAX_VALUE + 1) * 2 - diceValue + value - 1 :
                (Dice.MAX_VALUE + 1) * 2 + diceValue - value;

        for (int i = startNumber; i < startNumber + value; i++) {
            if ((i % (Dice.MAX_VALUE + 1)  < value - 1 && i % (Dice.MAX_VALUE + 1)  != 0)
                    || (i % (Dice.MAX_VALUE + 1) == Dice.MIN_VALUE && !enable))
                intSet.add(i);
            else if (i % (Dice.MAX_VALUE + 1) > value || (i % (Dice.MAX_VALUE + 1 ) == Dice.MAX_VALUE && !enable))
                intSet.add(i % Dice.MAX_VALUE);
        }
        return intSet;
    }

    /**
     * Players get notified that a certain player needs to modify a value of a certain dice
     * by +/- value.
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
        String response;
        int number;
        int minNumber;
        int maxNumber;

        minNumber = diceValue - value <= 0 ? ((value - diceValue) % 6) + 1 : diceValue - value;
        maxNumber = diceValue + value > 6 ? (diceValue + value) % 6 : diceValue + value;

        if (minNumber > maxNumber) {
            int temp;
            temp = minNumber;
            minNumber = maxNumber;
            maxNumber = temp;
        }

        do {
            response = getAnswer("Choose a number between 1 and 6:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number >= minNumber && number <= maxNumber) {
                networkManager.getGameController().setNewValue(number, gameName, toolCard.getName());
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
    }

    private void readRoundTrackParameters(RoundTrack roundTrack) throws RemoteException {
        String response;
        int roundNumber;
        int diceNumber;

        do {
            response = getAnswer("Choose a round:");
            try {
                roundNumber = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                roundNumber = -1;
            }
            if (roundNumber > 0 && roundNumber < RoundTrack.NUMBER_OF_TRACK) {
                response = getAnswer("Choose a dice:");
                try {
                    diceNumber = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    diceNumber = 0;
                }
                if (diceNumber > 0 && diceNumber < roundTrack.getDices(roundNumber - 1).size()) {
                    networkManager.getGameController().setDice(
                            roundTrack.getDices(roundNumber - 1).get(diceNumber - 1),
                            gameName, toolCard.getName());
                } else {
                    bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                    roundNumber = -1;
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                roundNumber = -1;
            }
        } while (roundNumber < 0);
    }

    @Override
    public void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> diceList = roundTrack.getDices(i);
            bufferManager.consolePrint("Round " + i + 1 + "\n", Level.LOW);
            printListDice(diceList, stringBuilder);
        }

        readRoundTrackParameters(roundTrack);
    }

    @Override
    public void notifyNeedPosition() throws RemoteException {

    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException {

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardMenuView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardMenuView that = (CLIToolCardMenuView) o;
        return Objects.equals(toolCard, that.toolCard);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), toolCard);
    }
}
