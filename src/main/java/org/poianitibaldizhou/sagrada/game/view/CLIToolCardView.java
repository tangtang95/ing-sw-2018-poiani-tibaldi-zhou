package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.*;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;

import java.rmi.RemoteException;
import java.util.*;

public class CLIToolCardView extends CLIMenuView implements IToolCardExecutorObserver, IToolCardObserver {
    private final transient ToolCard toolCard;
    private final String gameName;
    private final transient Player player;

    private static final String NUMBER_WARNING = "WARNING: Number is not correct";

    public CLIToolCardView(CLIGameView cliGameView, ToolCard toolCards)
            throws RemoteException {
        super(cliGameView.networkManager, cliGameView.screenManager);
        this.toolCard = toolCards;
        this.gameName = cliGameView.getGameName();
        this.player = cliGameView.getPlayer();

    }

    @Override
    public void notifyNeedDice(List<Dice> diceList) throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int number;

        bufferManager.consolePrint(buildGraphic.buildGraphicDices(diceList).toString(), Level.LOW);
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
        String response;
        int number;
        int minNumber;
        int maxNumber;

        minNumber = diceValue - value <= 0 ? (Dice.MAX_VALUE + 1 ) * 2 - value + diceValue - 1 : diceValue - value;
        maxNumber = diceValue + value > 6 ? (diceValue + value) % 6 : diceValue + value;

        if (minNumber > maxNumber) {
            int temp;
            temp = minNumber;
            minNumber = maxNumber;
            maxNumber = temp;
        }

        do {
            response = getAnswer("Choose the number " + minNumber + " or " + maxNumber + ":");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number == minNumber || number == maxNumber) {
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
        BuildGraphic buildGraphic = new BuildGraphic();

        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> diceList = roundTrack.getDices(i);
            bufferManager.consolePrint(
                    buildGraphic.buildMessage("Round " + i + 1 + "\n").buildGraphicDices(diceList).toString()
                    , Level.LOW);
        }

        readRoundTrackParameters(roundTrack);
    }

    @Override
    public void notifyNeedPosition() throws RemoteException {
        bufferManager.consolePrint(player.getSchemaCard().toString(), Level.LOW);

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
        if (!(o instanceof CLIToolCardView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardView that = (CLIToolCardView) o;
        return Objects.equals(toolCard, that.toolCard);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), toolCard);
    }
}
