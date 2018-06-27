package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * This class implement the IToolCardExecutorObserver and it takes care
 * of printing the notify of the toolCardAction on-screen and take the parameter for executing the command.
 */
public class CLIToolCardExecutorView extends UnicastRemoteObject implements IToolCardExecutorObserver {

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    private final transient ClientCreateMessage clientCreateMessage;

    /**
     * Reference to the ConsoleListener for reading number from keyword.
     */
    private final transient ConsoleListener consoleListener;

    /**
     * Network manager for connecting with the server.
     */
    private final transient ConnectionManager connectionManager;

    /**
     * Reference of name of tooCard associated with this class.
     */
    private final transient String toolCardName;

    private final transient GameModeStrategy gameModeStrategy;

    /**
     * Constructor.
     * Creates the view for viewing the actions during the execution of the tool card
     *
     * @param gameModeStrategy handles the differences between single player and multi player
     * @param toolCardName name of the executed tool card
     * @throws RemoteException rmi constructor throw
     */
    CLIToolCardExecutorView(GameModeStrategy gameModeStrategy, String toolCardName) throws RemoteException {
        super();
        this.consoleListener = ConsoleListener.getInstance();
        this.gameModeStrategy = gameModeStrategy;
        this.clientGetMessage = new ClientGetMessage();
        this.clientCreateMessage = new ClientCreateMessage();
        this.toolCardName = toolCardName;
        this.connectionManager = gameModeStrategy.getConnectionManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(String diceList) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(diceList);

        PrinterManager.consolePrint(buildGraphic.buildGraphicDices(diceWrapperList).
                buildMessage(ClientMessage.CHOOSE_DICE).toString(), Level.STANDARD);

        try {
            String message = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).
                    createGameNameMessage(gameModeStrategy.getGameName()).
                    createDiceMessage(diceWrapperList.get(
                            consoleListener.readNumber(diceWrapperList.size()))).buildMessage();
            connectionManager.getGameController().setDice(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue(String diceMessage) throws IOException {
        PrinterManager.consolePrint(ClientMessage.CHOOSE_NUMBER_BETWEEN_1_6, Level.STANDARD);

        try {
            String message = clientCreateMessage.createGameNameMessage(gameModeStrategy.getGameName()).
                    createTokenMessage(gameModeStrategy.getToken())
                    .createValueMessage(consoleListener.readValue(6)).buildMessage();
            connectionManager.getGameController().setNewValue(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(String colors) throws IOException {
        List<ColorWrapper> colorWrapperList = clientGetMessage.getColorList(colors);

        PrinterManager.consolePrint(ClientMessage.COLOR, Level.STANDARD);
        for (int i = 0; i < colorWrapperList.size(); i++) {
            PrinterManager.consolePrint("[" + (i + 1) + "] " + colorWrapperList.get(i) + "\n", Level.STANDARD);
        }
        PrinterManager.consolePrint(ClientMessage.CHOOSE_COLOR, Level.STANDARD);

        try {
            String message = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).
                    createGameNameMessage(gameModeStrategy.getGameName()).
                    createColorMessage(colorWrapperList.get(
                            consoleListener.readNumber(colorWrapperList.size()))).buildMessage();
            connectionManager.getGameController().setColor(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        int number;
        int minNumber;
        int maxNumber;

        int diceValue = clientGetMessage.getDice(message).getNumber();
        int value = clientGetMessage.getValue(message);

        minNumber = diceValue - value <= 0 ? (DiceWrapper.MAX_VALUE + 1) - value : diceValue - value;
        maxNumber = diceValue + value > 6 ? (diceValue + value) % 6 : diceValue + value;

        if (minNumber > maxNumber) {
            int temp;
            temp = minNumber;
            minNumber = maxNumber;
            maxNumber = temp;
        }

        if (!checkValidityDeltaValue(minNumber, diceValue, value)) {
            minNumber = maxNumber;
        } else if (!checkValidityDeltaValue(maxNumber, diceValue, value)) {
            maxNumber = minNumber;
        }

        try {
            do {
                if (minNumber != maxNumber)
                    PrinterManager.consolePrint(String.format(ClientMessage.CHOOSE_DELTA_NUMBER, minNumber, maxNumber), Level.STANDARD);
                else
                    PrinterManager.consolePrint(String.format(ClientMessage.CHOOSE_THE_NUMBER, minNumber), Level.STANDARD);
                number = consoleListener.readNumber(maxNumber);
                if (number == minNumber || number == maxNumber) {
                    String messageForController = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).
                            createGameNameMessage(gameModeStrategy.getGameName()).createValueMessage(number).buildMessage();
                    connectionManager.getGameController().setNewValue(messageForController);
                } else {
                    PrinterManager.consolePrint(ClientMessage.NOT_A_NUMBER, Level.STANDARD);
                    number = -1;
                }
            } while (number < 0);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDiceFromRoundTrack(String roundTrack) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        RoundTrackWrapper roundTrackWrapper = clientGetMessage.getRoundTrack(roundTrack);

        PrinterManager.consolePrint(buildGraphic.buildGraphicRoundTrack(roundTrackWrapper).toString(), Level.STANDARD);

        readRoundTrackParameters(roundTrackWrapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedPositionForRemoving(String message) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(message);

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_POSITION).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {
        consoleListener.stopCommandConsole();
        BuildGraphic buildGraphic = new BuildGraphic();

        ColorWrapper color = clientGetMessage.getColor(message);

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(message);

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_POSITION_WITH_COLOR
                + color.name()).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction() {
        PrinterManager.consolePrint(ClientMessage.COMMAND_ERROR, Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(String error) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.ERROR_TYPE + clientGetMessage.getCommandFlow(error)).
                buildMessage(String.format(ClientMessage.TOOL_CARD_ERROR, toolCardName)).toString(), Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedContinueAnswer() throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        boolean doAgain;
        boolean answer = false;

        do {
            PrinterManager.consolePrint(ClientMessage.CONTINUE_MESSAGE, Level.STANDARD);
            response = r.readLine();
            switch (response) {
                case "y":
                case "Y":
                    answer = true;
                    doAgain = false;
                    break;
                case "n":
                case "N":
                    doAgain = false;
                    break;
                default:
                    PrinterManager.consolePrint(ClientMessage.COMMAND_NOT_FOUND, Level.ERROR);
                    doAgain = true;
                    break;
            }
        } while (doAgain);

        String setMessage = clientCreateMessage.createGameNameMessage(gameModeStrategy.getGameName()).
                createTokenMessage(gameModeStrategy.getToken()).createBooleanMessage(answer).buildMessage();

        connectionManager.getGameController().setContinueAction(setMessage);
        consoleListener.wakeUpCommandConsole();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDiceReroll(String message) throws IOException {
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);

        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.RE_ROL_DICE).buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyExecutionEnded() {
        /* NOT NEEDED */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDicePouredOver(String message) throws IOException {
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);

        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.DICE_POURED_OVER).
                buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyWaitTurnEnd() {
        /* NOT DISPLAYED */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedPositionForPlacement(String message) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(message);
        DiceWrapper diceWrapper = clientGetMessage.getDice(message);

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_POSITION_ON_SCHEMA).
                buildGraphicDice(diceWrapper).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardPosition();
    }

    /**
     * Make the user input a row and a column (position) and sends it to the server
     *
     * @throws IOException if the reader not be able to get response by keyword.
     */
    private void schemaCardPosition() throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_POSITION_ON_SCHEMA).
                        buildMessage(ClientMessage.CHOOSE_ROW).toString(),
                Level.STANDARD);
        try {
            int row = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS);
            PrinterManager.consolePrint(ClientMessage.CHOOSE_COLUMN, Level.STANDARD);
            int column = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS);

            String setMessage = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).
                    createGameNameMessage(gameModeStrategy.getGameName()).createPositionMessage(
                    new PositionWrapper(row, column)).buildMessage();
            connectionManager.getGameController().setPosition(setMessage);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }

    }

    /**
     * Parse a round track, making the user choose a dice in it. It sends it to the server
     *
     * @param roundTrack round track that need to be parsed
     * @throws IOException error reading or network communication errors
     */
    private void readRoundTrackParameters(RoundTrackWrapper roundTrack) throws IOException {
        int roundNumber;
        try {

            do {
                PrinterManager.consolePrint(ClientMessage.CHOOSE_ROUND, Level.STANDARD);
                roundNumber = consoleListener.readNumber(RoundTrackWrapper.NUMBER_OF_TRACK);
            } while (roundTrack.getDicesForRound(roundNumber).isEmpty());

            PrinterManager.consolePrint(ClientMessage.CHOOSE_DICE, Level.STANDARD);
            int diceNumber = consoleListener.readNumber(roundTrack.getDicesForRound(roundNumber).size());

            String setRoundMessage = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).createGameNameMessage(gameModeStrategy.getGameName())
                    .createValueMessage(roundNumber).buildMessage();
            connectionManager.getGameController().setNewValue(setRoundMessage);

            String setDiceMessage = clientCreateMessage.createTokenMessage(gameModeStrategy.getToken()).
                    createGameNameMessage(gameModeStrategy.getGameName()).
                    createDiceMessage(roundTrack.getDicesForRound(roundNumber).get(diceNumber)).buildMessage();
            connectionManager.getGameController().setDice(setDiceMessage);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * Checks if the value is valid
     *
     * @param newValue new value
     * @param oldValue old value
     * @param delta    delta between values
     * @return true if valid, false otherwise
     */
    private boolean checkValidityDeltaValue(int newValue, int oldValue, int delta) {
        return !(newValue < DiceWrapper.MIN_VALUE || newValue > DiceWrapper.MAX_VALUE || (newValue != oldValue + delta) && (newValue != oldValue - delta));
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIToolCardExecutorView is the same or it has the same gameModeStrategy and same toolCardName.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardExecutorView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardExecutorView that = (CLIToolCardExecutorView) o;
        return Objects.equals(gameModeStrategy, that.gameModeStrategy) &&
                Objects.equals(toolCardName, that.toolCardName);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
