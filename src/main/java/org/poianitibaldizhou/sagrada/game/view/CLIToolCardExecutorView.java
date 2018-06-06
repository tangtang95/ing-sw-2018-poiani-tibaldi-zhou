package org.poianitibaldizhou.sagrada.game.view;

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

public class CLIToolCardExecutorView extends UnicastRemoteObject implements IToolCardExecutorObserver {

    private transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;
    private final transient ConsoleListener consoleListener;
    private final transient ConnectionManager connectionManager;
    private final transient String toolCardName;

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

        String message = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                createGameNameMessage(cliStateView.getGameName()).
                createDiceMessage(diceWrapperList.get(
                        consoleListener.readNumber(diceWrapperList.size() + 1))).buildMessage();
        connectionManager.getGameController().setDice(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue() throws IOException {
        PrinterManager.consolePrint("Choose a number between 1 and 6:\n", Level.STANDARD);

        String message = clientCreateMessage.createGameNameMessage(cliStateView.getGameName()).
                createTokenMessage(cliStateView.getToken())
                .createValueMessage(consoleListener.readNumber(6)).toString();
        connectionManager.getGameController().setNewValue(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(String colors) throws IOException {
        List<ColorWrapper> colorWrapperList = clientGetMessage.getColorList(colors);

        PrinterManager.consolePrint("Colors: \n", Level.STANDARD);
        for (int i = 0; i < colorWrapperList.size(); i++) {
            PrinterManager.consolePrint("[" + i + 1 + "] " + colorWrapperList.get(i) + "\n", Level.STANDARD);
        }
        PrinterManager.consolePrint("Choose a color: \n", Level.STANDARD);

        String message = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                createGameNameMessage(cliStateView.getGameName()).
                createColorMessage(colorWrapperList.get(
                        consoleListener.readNumber(colorWrapperList.size() + 1))).buildMessage();
        connectionManager.getGameController().setColor(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;
        int minNumber;
        int maxNumber;

        int diceValue = clientGetMessage.getDiceValue(message);
        int value = clientGetMessage.getValue(message);

        minNumber = diceValue - value <= 0 ? (DiceWrapper.MAX_VALUE + 1 ) * 2 - value + diceValue - 1 : diceValue - value;
        maxNumber = diceValue + value > 6 ? (diceValue + value) % 6 : diceValue + value;

        if (minNumber > maxNumber) {
            int temp;
            temp = minNumber;
            minNumber = maxNumber;
            maxNumber = temp;
        }

        do {
            PrinterManager.consolePrint("Choose the number " + minNumber + " or " + maxNumber + ":\n", Level.STANDARD);
            response = r.readLine();
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number == minNumber || number == maxNumber) {
                String messageForController  = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                        createGameNameMessage(cliStateView.getGameName()).createValueMessage(number).buildMessage();
                connectionManager.getGameController().setNewValue(messageForController);
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
        consoleListener.wakeUpCommandConsole();
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
    public void notifyNeedPosition() throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();

        String sendingParam = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                createGameNameMessage(cliStateView.getGameName()).buildMessage();
        String serverMessage = connectionManager.getGameController().getSchemaCardByToken(sendingParam);

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(serverMessage);

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

        String sendMessage = clientCreateMessage.createGameNameMessage(cliStateView.getGameName()).
                createTokenMessage(cliStateView.getToken()).buildMessage();
        String serverResponse = connectionManager.getGameController().getSchemaCardByToken(sendMessage);
        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(serverResponse);

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card with the color"
                + color.name()).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction(){
        PrinterManager.consolePrint("There was an error with the last command" +
                "which will be repeated.\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(String error){
        PrinterManager.consolePrint("You made an unforgivable mistake when using the Tool Card " +
                toolCardName + ", so you will not be able to use it this turn.\n", Level.INFORMATION);
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
        } while(doAgain);

        String setMessage = clientCreateMessage.createGameNameMessage(cliStateView.getGameName()).
                createTokenMessage(cliStateView.getToken()).createAnswerMessage(answer).buildMessage();

        connectionManager.getGameController().setContinueAction(setMessage);
        consoleListener.wakeUpCommandConsole();

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
        int row = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS + 1);
        PrinterManager.consolePrint("Choose a column:\n", Level.STANDARD);
        int column = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS + 1);

        String setMessage = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                createGameNameMessage(cliStateView.getGameName()).createPositionMessage(
                        new PositionWrapper(row, column)).buildMessage();
        connectionManager.getGameController().setPosition(setMessage);
    }

    /**
     * Parse a round track, making the user choose a dice in it. It sends it to the server
     *
     * @param roundTrack round track that need to be parsed
     * @throws IOException error reading or network communication errors
     */
    private void readRoundTrackParameters(RoundTrackWrapper roundTrack) throws IOException {
        PrinterManager.consolePrint("Choose a round: \n", Level.STANDARD);
        int roundNumber = consoleListener.readNumber(RoundTrackWrapper.NUMBER_OF_TRACK + 1);

        PrinterManager.consolePrint("Choose a dice: \n", Level.STANDARD);
        int diceNumber = consoleListener.readNumber(roundTrack.getDicesPerRound(roundNumber).size());

        String setMessage = clientCreateMessage.createTokenMessage(cliStateView.getToken()).
                createGameNameMessage(cliStateView.getGameName()).
                createDiceMessage(roundTrack.getDicesPerRound(roundNumber).get(diceNumber)).buildMessage();
        connectionManager.getGameController().setDice(setMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardExecutorView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardExecutorView that = (CLIToolCardExecutorView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage) &&
                Objects.equals(consoleListener, that.consoleListener) &&
                Objects.equals(connectionManager, that.connectionManager) &&
                Objects.equals(toolCardName, that.toolCardName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliStateView, clientGetMessage,
                clientCreateMessage, consoleListener, connectionManager, toolCardName);
    }
}
