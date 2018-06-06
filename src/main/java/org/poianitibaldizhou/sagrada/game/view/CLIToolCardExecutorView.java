package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
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

    private transient CLIStateScreen cliStateScreen;
    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;
    private final transient ConsoleListener consoleListener;
    private final transient ConnectionManager connectionManager;
    private final transient String toolCardName;

    public CLIToolCardExecutorView(CLIStateScreen cliStateScreen, String toolCardName) throws RemoteException {
        super();
        this.consoleListener = ConsoleListener.getInstance();
        this.cliStateScreen = cliStateScreen;
        this.clientGetMessage =cliStateScreen.getClientGetMessage();
        this.clientCreateMessage = cliStateScreen.getClientCreateMessage();
        this.toolCardName = toolCardName;
        this.connectionManager = cliStateScreen.connectionManager;
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(String diceList) throws IOException {
        consoleListener.stopCommandConsole();
        BuildGraphic buildGraphic = new BuildGraphic();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;

        List<DiceWrapper> diceWrapperList = clientGetMessage.getDiceList(diceList);

        PrinterManager.consolePrint(buildGraphic.buildGraphicDices(diceWrapperList).toString(), Level.STANDARD);
        do {
            PrinterManager.consolePrint("Choose a dice: ", Level.STANDARD);
            response = r.readLine();
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < diceWrapperList.size()) {
                String message = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                        createGameNameMessage(cliStateScreen.getGameName()).createDiceMessage(diceWrapperList.get(number-1)).buildMessage();
                connectionManager.getGameController().setDice(message);
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
    public void notifyNeedNewValue() throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;
        consoleListener.stopCommandConsole();
        do {
            PrinterManager.consolePrint("Choose a number between 1 and 6:", Level.STANDARD);
            response = r.readLine();
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < 7) {
                String message = clientCreateMessage.createGameNameMessage(cliStateScreen.getGameName()).
                        createTokenMessage(cliStateScreen.getToken()).createValueMessage(number).toString();
                connectionManager.getGameController().setNewValue(message);
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
    public void notifyNeedColor(String colors) throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;

        List<ColorWrapper> colorWrapperList = clientGetMessage.getColorList(colors);

        PrinterManager.consolePrint("Colors: ", Level.STANDARD);
        for (int i = 0; i < colorWrapperList.size(); i++) {
            PrinterManager.consolePrint("[" + i + 1 + "] " + colorWrapperList.get(i) + "\n", Level.STANDARD);
        }

        do {
            PrinterManager.consolePrint("Choose a color: ", Level.STANDARD);
            response = r.readLine();
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number < 7) {
                String message = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                        createGameNameMessage(cliStateScreen.getGameName()).createColorMessage(colorWrapperList.get(number-1)).buildMessage();
                connectionManager.getGameController().setColor(message);
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
            PrinterManager.consolePrint("Choose the number " + minNumber + " or " + maxNumber + ":", Level.STANDARD);
            response = r.readLine();
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number == minNumber || number == maxNumber) {
                String messageForController  = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                        createGameNameMessage(cliStateScreen.getGameName()).createValueMessage(number).buildMessage();
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
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        BuildGraphic buildGraphic = new BuildGraphic();

        String sendingParam = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                createGameNameMessage(cliStateScreen.getGameName()).buildMessage();
        String serverMessage = connectionManager.getGameController().getSchemaCardByToken(sendingParam);

        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(serverMessage);

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position on your Schema Card").
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardCLI(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDicePositionOfCertainColor(String message) throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        BuildGraphic buildGraphic = new BuildGraphic();

        ColorWrapper color = clientGetMessage.getColor(message);

        String sendMessage = clientCreateMessage.createGameNameMessage(cliStateScreen.getGameName()).
                createTokenMessage(cliStateScreen.getToken()).buildMessage();
        String serverResponse = connectionManager.getGameController().getSchemaCardByToken(sendMessage);
        SchemaCardWrapper schemaCard = clientGetMessage.getSchemaCard(serverResponse);

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card with the color"
                + color.name()).
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
        schemaCardCLI(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRepeatAction() throws IOException {
        PrinterManager.consolePrint("WARNING: There was an error with the last command\n " +
                "which will be repeated.", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCommandInterrupted(String error) throws IOException {
        PrinterManager.consolePrint("You made an unforgivable mistake when using the Tool Card " +
                toolCardName + ", so you will not be able to use it this turn.", Level.INFORMATION);
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
            PrinterManager.consolePrint("Do you want to continue? (y/n)", Level.STANDARD);
            response = r.readLine();
            if(response.equals("y") || response.equals("Y")) {
                answer = true;
                doAgain = false;
            } else if(response.equals("n") || response.equals("N")) {
                answer = false;
                doAgain = false;
            } else {
                PrinterManager.consolePrint("Incorrect input", Level.STANDARD);
                doAgain = true;
            }
        } while(doAgain);

        String setMessage = clientCreateMessage.createGameNameMessage(cliStateScreen.getGameName()).
                createTokenMessage(cliStateScreen.getToken()).createAnswerMessage(answer).buildMessage();

        connectionManager.getGameController().setContinueAction(setMessage);
        consoleListener.wakeUpCommandConsole();

    }

    /**
     * Make the user input a row and a column (position) and sends it to the server
     *
     * @param r buffer reader for reading from console
     * @throws IOException
     */
    private void schemaCardCLI(BufferedReader r) throws IOException {
        consoleListener.stopCommandConsole();
        String response;
        int row;
        int column;
        do {
            PrinterManager.consolePrint("Insert a row: ", Level.STANDARD);
            response = r.readLine();
            try {
                row = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                row = -1;
            }
            if (row > 0 && row <= SchemaCardWrapper.NUMBER_OF_ROWS) {
                PrinterManager.consolePrint("Insert a column: ", Level.STANDARD);
                response = r.readLine();
                try {
                    column = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    column = 0;
                }
                if (column > 0 && column <= SchemaCardWrapper.NUMBER_OF_COLUMNS) {
                    String setMessage = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                            createGameNameMessage(cliStateScreen.getGameName()).createPositionMessage(new PositionWrapper(row, column)).buildMessage();
                    connectionManager.getGameController().setPosition(setMessage);
                } else {
                    PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                    row = -1;
                }
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                row = -1;
            }
        } while (row < 0);
        consoleListener.wakeUpCommandConsole();
    }

    /**
     * Parse a round track, making the user choose a dice in it. It sends it to the server
     *
     * @param roundTrack round track that need to be parsed
     * @throws IOException error reading or network communication errors
     */
    private void readRoundTrackParameters(RoundTrackWrapper roundTrack) throws IOException {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int roundNumber;
        int diceNumber;

        do {
            PrinterManager.consolePrint("Choose a round: ", Level.STANDARD);
            response = r.readLine();
            try {
                roundNumber = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                roundNumber = -1;
            }
            if (roundNumber > 0 && roundNumber < RoundTrack.NUMBER_OF_TRACK) {
                PrinterManager.consolePrint("Choose a dice: ", Level.STANDARD);
                response = r.readLine();
                try {
                    diceNumber = Integer.parseInt(response);
                } catch (NumberFormatException e) {
                    diceNumber = 0;
                }
                if (diceNumber > 0 && diceNumber < roundTrack.getDicesPerRound(roundNumber - 1).size()) {
                    String setMessage = clientCreateMessage.createTokenMessage(cliStateScreen.getToken()).
                            createGameNameMessage(cliStateScreen.getGameName()).
                            createDiceMessage(roundTrack.getDicesPerRound(roundNumber - 1).get(diceNumber-1)).buildMessage();
                    connectionManager.getGameController().setDice(setMessage);
                } else {
                    PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                    roundNumber = -1;
                }
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                roundNumber = -1;
            }
        } while (roundNumber < 0);
        consoleListener.wakeUpCommandConsole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIToolCardExecutorView)) return false;
        if (!super.equals(o)) return false;
        CLIToolCardExecutorView that = (CLIToolCardExecutorView) o;
        return Objects.equals(cliStateScreen, that.cliStateScreen) &&
                Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage) &&
                Objects.equals(consoleListener, that.consoleListener) &&
                Objects.equals(connectionManager, that.connectionManager) &&
                Objects.equals(toolCardName, that.toolCardName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliStateScreen, clientGetMessage,
                clientCreateMessage, consoleListener, connectionManager, toolCardName);
    }
}
