package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.*;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;

import java.rmi.RemoteException;
import java.util.*;

public class CLIToolCardView extends CLIMenuView implements IToolCardExecutorObserver, IToolCardObserver {
    private final transient ToolCard toolCard;
    private final transient SchemaCard schemaCard;
    private final String gameName;

    private static final String CHOOSE_DICE = "Choose a dice:";

    CLIToolCardView(CLIGameView cliGameView, ToolCard toolCards)
            throws RemoteException {
        super(cliGameView.networkManager, cliGameView.screenManager, cliGameView.bufferManager);
        this.toolCard = toolCards;
        this.gameName = cliGameView.getGameName();
        this.schemaCard = cliGameView.getCliSchemaCardView().getSchemaCard(cliGameView.getCurrentUser().getName());

    }

    @Override
    public void notifyNeedDice(List<Dice> diceList) throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int number;

        bufferManager.consolePrint(buildGraphic.buildGraphicDices(diceList).toString(), Level.LOW);
        do {
            response = getAnswer(CHOOSE_DICE);
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < diceList.size()) {
                //networkManager.getGameController().setDice(diceList.get(number - 1), gameName, toolCard.getName());
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
                //networkManager.getGameController().setNewValue(number, gameName, toolCard.getName());
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
                //networkManager.getGameController().setColor((Color) colors.toArray()[number - 1],
                        //gameName, toolCard.getName());
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
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
                //networkManager.getGameController().setNewValue(number, gameName, toolCard.getName());
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
                response = getAnswer(CHOOSE_DICE);
                try {
                    diceNumber = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    diceNumber = 0;
                }
                if (diceNumber > 0 && diceNumber < roundTrack.getDices(roundNumber - 1).size()) {
                    //networkManager.getGameController().setDice(
                            //roundTrack.getDices(roundNumber - 1).get(diceNumber - 1),
                            //gameName, toolCard.getName());
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

        bufferManager.consolePrint(buildGraphic.buildGraphicRoundTrack(roundTrack).toString(), Level.LOW);

        readRoundTrackParameters(roundTrack);
    }

    @Override
    public void notifyNeedPosition() throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int row;
        int column;

        bufferManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").
                buildMessage(schemaCard.toString()).toString(), Level.LOW);
        do {
            response = getAnswer("Insert a row: ");
            try {
                row = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                row = -1;
            }
            if (row > 0 && row <= SchemaCard.NUMBER_OF_ROWS) {
                response = getAnswer("Insert a column: ");
                try {
                    column = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    column = 0;
                }
                if (column > 0 && column <= SchemaCard.NUMBER_OF_COLUMNS) {
                    //networkManager.getGameController().setPosition(new Position(row,column),gameName,
                            //toolCard.getName());
                } else {
                    bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                    row = -1;
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                row = -1;
            }
        } while (row < 0);
    }

    @Override
    public void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int row;
        int column;

        bufferManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card with the color"
                + color.name()).
                buildMessage(schemaCard.toString()).toString(), Level.LOW);
        do {
            response = getAnswer("Insert a row: ");
            try {
                row = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                row = -1;
            }
            if (row > 0 && row <= SchemaCard.NUMBER_OF_ROWS) {
                response = getAnswer("Insert a column: ");
                try {
                    column = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    column = 0;
                }
                if (column > 0 && column <= SchemaCard.NUMBER_OF_COLUMNS) {
                    //networkManager.getGameController().setPosition(new Position(row,column),gameName,
                            //toolCard.getName());
                } else {
                    bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                    row = -1;
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                row = -1;
            }
        } while (row < 0);
    }

    public void notifyRepeatAction() {
        bufferManager.consolePrint("WARNING: There was an error with the last command\n " +
                "which will be repeated.", Level.HIGH);
    }

    @Override
    public void notifyCommandInterrupted(CommandFlow error) {
        bufferManager.consolePrint("You made an unforgivable mistake when using the Tool Card " +
                toolCard.getName() + ", so you will not be able to use it this turn.", Level.HIGH);
    }

    @Override
    public void notifyNeedContinueAnswer() throws RemoteException {

    }

    @Override
    public void onTokenChange(int tokens) {
        bufferManager.consolePrint("Now the Tool Card " + toolCard.getName() +
                "have got " + tokens + "tokens on it", Level.HIGH);
    }

    @Override
    public void onCardDestroy() {
        bufferManager.consolePrint("From now on you will no longer be able to use the Tool Card " +
                toolCard.getName() + "in this game.", Level.HIGH);
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
