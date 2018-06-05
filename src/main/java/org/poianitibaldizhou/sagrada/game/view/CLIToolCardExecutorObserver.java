package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CLIToolCardExecutorObserver extends UnicastRemoteObject implements IToolCardExecutorObserver {

    private final transient CLIGameView cliGameView;
    private final transient ConsoleListener consoleListener;
    private final transient String toolCardName;

    public CLIToolCardExecutorObserver(CLIGameView cliGameView, String toolCardName) throws RemoteException {
        super();
        this.consoleListener = ConsoleListener.getInstance();
        this.cliGameView = cliGameView;
        this.toolCardName = toolCardName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDice(String diceList) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;

        List<DiceWrapper> diceWrapperList = cliGameView.getClientGetMessage().getDiceList(diceList);

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
                String message = cliGameView.getClientCreateMessage().createTokenMessage(cliGameView.getToken()).
                        createGameNameMessage(cliGameView.getGameName()).createDiceMessage(diceWrapperList.get(number-1)).buildMessage();
                cliGameView.getConnectionManager().getGameController().setDice(message);
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewValue() throws IOException {
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
                String message = cliGameView.getClientCreateMessage().createGameNameMessage(cliGameView.getGameName()).
                        createTokenMessage(cliGameView.getToken()).createValueMessage(number).toString();
                cliGameView.getConnectionManager().getGameController().setNewValue(message);
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedColor(String colors) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;

        List<ColorWrapper> colorWrapperList = cliGameView.getClientGetMessage().getColorList(colors);

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
                String message = cliGameView.getClientCreateMessage().createTokenMessage(cliGameView.getToken()).
                        createGameNameMessage(cliGameView.getGameName()).createColorMessage(colorWrapperList.get(number-1)).buildMessage();
                cliGameView.getConnectionManager().getGameController().setColor(message);
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedNewDeltaForDice(String message) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String response;
        int number;
        int minNumber;
        int maxNumber;

        int diceValue = cliGameView.getClientGetMessage().getDiceValue(message);
        int value = cliGameView.getClientGetMessage().getValue(message);

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
                String messageForController  = cliGameView.getClientCreateMessage().createTokenMessage(cliGameView.getToken()).
                        createGameNameMessage(cliGameView.getGameName()).createValueMessage(number).buildMessage();
                cliGameView.getConnectionManager().getGameController().setNewValue(messageForController);
            } else {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyNeedDiceFromRoundTrack(String roundTrack) throws IOException {

    }


    /**
     * {@inheritDoc}
     */
    public void notifyNeedPosition() throws IOException {}
    /*
    @Override
    public void notifyNeedPosition() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int row;
        int column;


        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position on your Schema Card").
                buildMessage(schemaCard.toString()).toString(), Level.STANDARD);
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
                if (column > 0 && column <= SchemaCard.NUMBER_OF_COLUMNS) {
                    connectionManager.getGameController().setPosition( currentUser.getToken(), gameName,
                            new Position(row,column),
                            toolCard.getName());
                } else {
                    PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                    row = -1;
                }
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                row = -1;
            }
        } while (row < 0);
    }*/

    @Override
    public void notifyNeedDicePositionOfCertainColor(String color) throws IOException {

    }

    @Override
    public void notifyRepeatAction() throws IOException {

    }

    @Override
    public void notifyCommandInterrupted(String error) throws IOException {

    }

    @Override
    public void notifyNeedContinueAnswer() throws IOException {

    }
}
