package org.poianitibaldizhou.sagrada.game.view;

import edu.emory.mathcs.backport.java.util.concurrent.TimeoutException;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

/**
 * This class implement the IToolCardExecutorObserver and it takes care
 * of printing the notify of the toolCardAction on-screen and take the parameter for executing the command.
 */
public class CLIToolCardExecutorView extends UnicastRemoteObject implements IToolCardExecutorObserver {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private transient CLIStateView cliStateView;

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

    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @param toolCardName the name of toolCard associated at this class.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIToolCardExecutorView(CLIStateView cliStateView, String toolCardName) throws RemoteException {
        super();
        this.consoleListener = ConsoleListener.getInstance();
        this.cliStateView = cliStateView;
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.clientCreateMessage = cliStateView.getClientCreateMessage();
        this.toolCardName = toolCardName;
        this.connectionManager = cliStateView.getConnectionManager();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(String diceList) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(diceList);

        PrinterManager.consolePrint(buildGraphic.buildGraphicDices(diceWrapperList).
                buildMessage("Choose a dice: ").toString(), Level.STANDARD);

        try {
            String message = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                    createGameNameMessage(cliStateView.getGameName()).
                    createDiceMessage(diceWrapperList.get(
                            consoleListener.readNumber(diceWrapperList.size()))).buildMessage();
            connectionManager.getGameController().setDice(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue() throws IOException {
        PrinterManager.consolePrint("Choose a number between 1 and 6:\n", Level.STANDARD);

        try {
            String message = clientCreateMessage.createGameNameMessage(cliStateView.getGameName()).
                    createTokenMessage(cliStateView.getToken())
                    .createValueMessage(consoleListener.readValue(6)).buildMessage();
            connectionManager.getGameController().setNewValue(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(String colors) throws IOException {
        List<ColorWrapper> colorWrapperList = clientGetMessage.getColorList(colors);

        PrinterManager.consolePrint("Colors: \n", Level.STANDARD);
        for (int i = 0; i < colorWrapperList.size(); i++) {
            PrinterManager.consolePrint("[" + (i + 1) + "] " + colorWrapperList.get(i) + "\n", Level.STANDARD);
        }
        PrinterManager.consolePrint("Choose a color: \n", Level.STANDARD);

        try {
            String message = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                    createGameNameMessage(cliStateView.getGameName()).
                    createColorMessage(colorWrapperList.get(
                            consoleListener.readNumber(colorWrapperList.size()))).buildMessage();
            connectionManager.getGameController().setColor(message);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
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

        int diceValue = clientGetMessage.getDiceValue(message);
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
                    PrinterManager.consolePrint("Choose the number " + minNumber + " or " + maxNumber + ":\n", Level.STANDARD);
                else
                    PrinterManager.consolePrint("Choose the number " + minNumber + ":\n", Level.STANDARD);
                number = consoleListener.readNumber(maxNumber);
                if (number == minNumber || number == maxNumber) {
                    String messageForController = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                            createGameNameMessage(cliStateView.getGameName()).createValueMessage(number).buildMessage();
                    connectionManager.getGameController().setNewValue(messageForController);
                } else {
                    PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                    number = -1;
                }
            } while (number < 0);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
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
    public void notifyNeedPosition(String message) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(message);

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position on your Schema Card\n").
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

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card with the color"
                + color.name()).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction() {
        PrinterManager.consolePrint("There was an error with the last command" +
                "which will be repeated.\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(String error) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.buildMessage("ERROR TYPE: " + clientGetMessage.getCommandFlow(error)).
                buildMessage("You made an unforgivable mistake when using the Tool Card " +
                        toolCardName + ", so you will not be able to use it this turn.").toString(), Level.INFORMATION);
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
            PrinterManager.consolePrint("Do you want to continue? (y/n)\n", Level.STANDARD);
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
                    PrinterManager.consolePrint("Incorrect input\n", Level.ERROR);
                    doAgain = true;
                    break;
            }
        } while (doAgain);

        System.out.println("ANSWER IN VIEW: " + answer);

        String setMessage = clientCreateMessage.createGameNameMessage(cliStateView.getGameName()).
                createTokenMessage(cliStateView.getToken()).createBooleanMessage(answer).buildMessage();

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
        PrinterManager.consolePrint(buildGraphic.buildMessage("The dice has been re-rolled: ").buildGraphicDice(diceWrapper).toString(), Level.STANDARD);
    }

    /**
     * Make the user input a row and a column (position) and sends it to the server
     *
     * @throws IOException if the reader not be able to get response by keyword.
     */
    private void schemaCardPosition() throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").
                        buildMessage("Choose a row:").toString(),
                Level.STANDARD);
        try {
            int row = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS);
            PrinterManager.consolePrint("Choose a column:\n", Level.STANDARD);
            int column = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS);

            String setMessage = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                    createGameNameMessage(cliStateView.getGameName()).createPositionMessage(
                    new PositionWrapper(row, column)).buildMessage();
            connectionManager.getGameController().setPosition(setMessage);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
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
                PrinterManager.consolePrint("Choose a round: \n", Level.STANDARD);
                roundNumber = consoleListener.readNumber(RoundTrackWrapper.NUMBER_OF_TRACK);
            } while (roundTrack.getDicesPerRound(roundNumber).isEmpty());

            PrinterManager.consolePrint("Choose a dice: \n", Level.STANDARD);
            int diceNumber = consoleListener.readNumber(roundTrack.getDicesPerRound(roundNumber).size());

            String setRoundMessage = clientCreateMessage.createTokenMessage(cliStateView.getToken()).createGameNameMessage(cliStateView.getGameName())
                    .createValueMessage(roundNumber).buildMessage();
            connectionManager.getGameController().setNewValue(setRoundMessage);

            String setDiceMessage = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                    createGameNameMessage(cliStateView.getGameName()).
                    createDiceMessage(roundTrack.getDicesPerRound(roundNumber).get(diceNumber)).buildMessage();
            connectionManager.getGameController().setDice(setDiceMessage);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
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
        return newValue < DiceWrapper.MIN_VALUE || newValue > DiceWrapper.MAX_VALUE || (newValue != oldValue + delta) && (newValue != oldValue - delta);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIToolCardExecutorView is the same or it has the same cliStateView and same toolCardName.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardExecutorView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardExecutorView that = (CLIToolCardExecutorView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
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
