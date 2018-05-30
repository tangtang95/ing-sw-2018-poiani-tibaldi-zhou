package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;

public class CLITurnScreen extends CLIRoundScreen {

    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @param gameName
     * @param myUser
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLITurnScreen(ConnectionManager networkManager, ScreenManager screenManager, String gameName, User myUser)
            throws RemoteException {
        super(networkManager, screenManager, gameName, myUser);

        initializeCommands();
    }

    @Override
    protected void initializeCommands() {
        super.initializeCommands();

        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card from Draft Pool");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);
    }

    @Override
    public void startCLI() {
        super.startCLI();
    }

    /*
    private Position selectPosition() {

        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        Position position = null;

        int row;
        int column;

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").toString(),
                Level.STANDARD);
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
                    position = new Position(row, column);
                } else {
                    PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                    row = -1;
                }
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                row = -1;
            }
        } while (row < 0);
        return position;
    }
    */

    private void placeDice() {/*
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int diceNumber;

        PrinterManager.consolePrint(buildGraphic.
                buildMessage("---------------------------DRAFT POOL---------------------------").
                buildGraphicDices(cliDraftPoolView.getDraftPool().getDices()).
                buildMessage("\n---------------------------SCHEMA CARD---------------------------").
                buildMessage(cliSchemaCardView.getSchemaCard(currentUser.getName()).toString()).
                toString(), Level.STANDARD);
        do {
            response = getAnswer("Choose a dice to place in your schema card: ");
            try {
                diceNumber = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                diceNumber = -1;
            }
            if (diceNumber > 0 && diceNumber < cliDraftPoolView.getDraftPool().getDices().size()) {
                try {
                    connectionManager.getGameController().placeDice(currentUser.getToken(),
                            gameName, cliDraftPoolView.getDraftPool().getDices().get(diceNumber - 1),
                            selectPosition());
                } catch (RemoteException e) {
                    PrinterManager.consolePrint("NETWORK ERROR", Level.INFORMATION);
                } catch (IOException e) {
                    PrinterManager.consolePrint("NETWORK ERROR", Level.ERROR);
                }
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                diceNumber = -1;
            }
        } while (diceNumber < 0);
        */

    }

    private void playToolCard() throws RemoteException {/*
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int number;

        PrinterManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCards).toString(), Level.STANDARD);
        do {
            response = getAnswer("Choose a Tool Card:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number <= toolCards.size()) {
                try {
                    connectionManager.getGameController().useToolCard(currentUser.getToken(),
                            gameName, toolCards.get(number - 1),
                            new CLIToolCardView(this, toolCards.get(number - 1)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connectionManager.getGameController().useToolCard(currentUser.getToken(),
                            gameName, toolCards.get(number - 1),
                            new CLIToolCardView(this,toolCards.get(number - 1)));
                } catch (IOException e) {
                    // TODO handle exception
                }
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
        */
    }

}
