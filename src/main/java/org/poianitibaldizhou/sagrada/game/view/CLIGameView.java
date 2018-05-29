package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;

import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CLIGameView extends CLIBasicView implements IGameView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private transient List<ToolCard> toolCards;
    private transient List<PublicObjectiveCard> publicObjectiveCards;
    private transient List<PrivateObjectiveCard> privateObjectiveCards;
    private final transient User myUser;
    private transient User currentUser;
    private String gameName;

    private final transient CLISchemaCardView cliSchemaCardView;
    private final transient CLIDraftPoolView cliDraftPoolView;
    private final transient CLIRoundTrackView cliRoundTrackView;

    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";
    private static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";


    public CLIGameView(ConnectionManager connectionManager, ScreenManager screenManager,
                       String gameName, User myUser)
            throws RemoteException {
        super(connectionManager, screenManager);
        this.gameName = gameName;
        this.myUser = myUser;

        this.cliSchemaCardView = new CLISchemaCardView(this);
        this.cliDraftPoolView = new CLIDraftPoolView(this);
        this.cliRoundTrackView = new CLIRoundTrackView(this);

        initializeCommands();
    }


    private void initializeCommands() {
        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card from Draft Pool");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(screenManager::popScreen);
        commandMap.put(quit.getCommandText(), quit);

        Command viewDraftPool = new Command(VIEW_DRAFT_POOL, "View the game Draft Pool");
        viewDraftPool.setCommandAction(this::viewDraftPool);
        commandMap.put(viewDraftPool.getCommandText(), viewDraftPool);

        Command viewRoundTrack = new Command(VIEW_ROUND_TRACK, "View the game Round Track");
        viewRoundTrack.setCommandAction(this::viewRoundTrack);
        commandMap.put(viewRoundTrack.getCommandText(), viewRoundTrack);

        Command viewToolCards = new Command(VIEW_TOOL_CARDS, "View the Tool Cards playable");
        viewToolCards.setCommandAction(this::viewToolCards);
        commandMap.put(viewToolCards.getCommandText(), viewToolCards);

        Command viewPublicObjectiveCards = new Command(VIEW_PUBLIC_OBJECTIVE_CARD,
                "View the Public Objective cards");
        viewPublicObjectiveCards.setCommandAction(this::viewPublicObjectiveCards);
        commandMap.put(viewPublicObjectiveCards.getCommandText(), viewPublicObjectiveCards);

        Command viewSchemaCards = new Command(VIEW_SCHEMA_CARDS, "View the Schema card of all players");
        viewSchemaCards.setCommandAction(this::viewSchemaCards);
        commandMap.put(viewSchemaCards.getCommandText(), viewSchemaCards);

        Command viewMySchema = new Command(VIEW_MY_SCHEMA, "View my Schema card");
        viewMySchema.setCommandAction(this::viewMySchemaCard);
        commandMap.put(viewMySchema.getCommandText(), viewMySchema);

        Command viewPrivateObjectiveCards = new Command(VIEW_PRIVATE_OBJECTIVE_CARD,
                "View the Private Objective cards");
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);
    }

    @Override
    public void run() {
        BuildGraphic buildGraphic = new BuildGraphic();
        try {
            networkManager.getGameController().joinGame(myUser.getToken(),
                    gameName,
                    this,
                    this,
                    cliRoundTrackView,
                    new CLIStateView(this),
                    cliDraftPoolView,
                    new CLIDiceBagView(this));
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("-----------------------------WELCOME-------------------------------").
                        buildGraphicHelp(commandMap).
                        buildMessage("Choose the action: ").toString(),
                Level.STANDARD);
        /*
        try {
            sendCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }catch (NullPointerException e) {
            //...
        }
        */
    }

    private void viewPrivateObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.buildGraphicPrivateObjectiveCards(privateObjectiveCards).
                toString(), Level.STANDARD);
    }

    private void viewMySchemaCard() {
        PrinterManager.consolePrint(cliSchemaCardView.getSchemaCard(myUser.getName()).toString(), Level.STANDARD);
    }

    private void viewSchemaCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        for (String userName : cliSchemaCardView.getSchemaCards().keySet()) {
            PrinterManager.consolePrint(buildGraphic.buildMessage("Player: " + userName).
                    buildMessage(cliSchemaCardView.getSchemaCard(userName).toString()).
                    buildMessage("").toString(), Level.STANDARD);
        }
    }

    private void viewPublicObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.buildGraphicPublicObjectiveCards(publicObjectiveCards).
                toString(), Level.STANDARD);
    }

    private void viewToolCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCards).toString(), Level.STANDARD);
    }

    private void viewRoundTrack() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                buildGraphicRoundTrack(cliRoundTrackView.getRoundTrack()).toString(), Level.STANDARD);
    }

    private void viewDraftPool() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                buildMessage("---------------------------DRAFT POOL---------------------------").
                buildGraphicDices(cliDraftPoolView.getDraftPool().getDices()).
                toString(), Level.STANDARD);
    }

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

    private void placeDice() {
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
                    networkManager.getGameController().placeDice(currentUser.getToken(),
                            gameName, cliDraftPoolView.getDraftPool().getDices().get(diceNumber - 1),
                            selectPosition());
                } catch (RemoteException e) {
                    PrinterManager.consolePrint("NETWORK ERROR", Level.INFORMATION);
                }
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                diceNumber = -1;
            }
        } while (diceNumber < 0);

    }

    private void playToolCard() throws RemoteException {
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
                networkManager.getGameController().useToolCard(currentUser.getToken(),
                        gameName, toolCards.get(number - 1),
                        new CLIToolCardView(this, toolCards.get(number - 1)));
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
    }

    @Override
    public void onPlayersCreate(List<Player> players) {
        BuildGraphic buildGraphic = new BuildGraphic();
        buildGraphic.buildMessage("-----------------------------PLAYER-----------------------------");
        for (Player p : players) {
            buildGraphic.buildMessage(p.getUser().getName());
            PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);
            cliSchemaCardView.addSchemaCard(p.getUser().getName(), p.getSchemaCard());
            try {
                networkManager.getGameController().bindPlayer(p.getToken(),
                        p.getUser().getName(),
                        p,
                        new CLIPlayerObserver(this),
                        new CLISchemaCardView(this));
            } catch (RemoteException e) {
                Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
            }
        }
    }

    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
        for (ToolCard toolCard : toolCards) {
            try {
                networkManager.getGameController().bindToolCard(myUser.getToken(),
                        gameName, toolCard, new CLIToolCardView(this, toolCard));
            } catch (RemoteException e) {
                Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
            }
        }
    }

    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) {
        //...
    }

    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) {
        this.privateObjectiveCards = privateObjectiveCards;
    }

    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Mamma " + bufferedReader.readLine());
        /*
        BuildGraphic buildGraphic = new BuildGraphic();
        List<SchemaCard> schemaCardList = new ArrayList<>();
        String response;
        int number;

        for (List<SchemaCard> ls : schemaCards)
            schemaCardList.addAll(ls);

        buildGraphic.buildMessage("----------------------------SCHEMA CARDS----------------------------");
        for (int i = 0; i < schemaCardList.size(); i++) {
            buildGraphic.buildMessage("                   [" + (i + 1) + "]").
                    buildMessage(schemaCardList.get(i).toString());
        }
        PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);

        do {
            response = getAnswer("Choose a Schema card:");
            try {
                number = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number > 0 && number <= schemaCardList.size()) {
                networkManager.getGameController().chooseSchemaCard(myUser.getToken(), gameName,
                        schemaCardList.get(number - 1));
            } else {
                PrinterManager.consolePrint(NUMBER_WARNING, Level.STANDARD);
                number = -1;
            }
        } while (number < 0);
        */

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getGameName() {
        return gameName;
    }

    public CLISchemaCardView getCliSchemaCardView() {
        return cliSchemaCardView;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint("INFORMATION: " + ack, Level.INFORMATION);
    }

    @Override
    public void err(String err) {
        PrinterManager.consolePrint("ERROR: " + err, Level.INFORMATION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIGameView)) return false;
        if (!super.equals(o)) return false;
        CLIGameView that = (CLIGameView) o;
        return Objects.equals(commandMap, that.commandMap) &&
                Objects.equals(toolCards, that.toolCards) &&
                Objects.equals(publicObjectiveCards, that.publicObjectiveCards) &&
                Objects.equals(getCurrentUser(), that.getCurrentUser()) &&
                Objects.equals(getGameName(), that.getGameName()) &&
                Objects.equals(getCliSchemaCardView(), that.getCliSchemaCardView()) &&
                Objects.equals(cliDraftPoolView, that.cliDraftPoolView) &&
                Objects.equals(cliRoundTrackView, that.cliRoundTrackView);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap, toolCards, publicObjectiveCards,
                getCurrentUser(), getGameName(), getCliSchemaCardView(), cliDraftPoolView, cliRoundTrackView);
    }
}
