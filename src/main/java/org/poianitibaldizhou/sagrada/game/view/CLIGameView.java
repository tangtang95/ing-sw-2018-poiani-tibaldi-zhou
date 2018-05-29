package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;

import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CLIGameView extends CLIMenuView implements IGameView{
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



    public CLIGameView(ConnectionManager connectionManager, ScreenManager screenManager, BufferManager bufferManager,
                       String gameName, User myUser)
            throws RemoteException {
        super(connectionManager, screenManager, bufferManager);
        this.gameName =gameName;
        this.myUser = myUser;

        this.cliSchemaCardView = new CLISchemaCardView(this);
        this.cliDraftPoolView = new CLIDraftPoolView(this);
        new CLIStateView(this);
        new CLIDiceBagView(this);
        new CLIPlayerObserver(this);
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

        bufferManager.consolePrint(buildGraphic.
                        buildMessage("-----------------------------WELCOME-------------------------------").
                        buildGraphicHelp(commandMap).
                        buildMessage("Choose the action: ").toString(),
                Level.LOW);

        try {
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }catch (NullPointerException e) {
            //...
        }

    }

    private void viewPrivateObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.buildGraphicPrivateObjectiveCards(privateObjectiveCards).
                toString(), Level.LOW);
    }

    private void viewMySchemaCard() {
        bufferManager.consolePrint(cliSchemaCardView.getSchemaCard(myUser.getName()).toString(), Level.LOW);
    }

    private void viewSchemaCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        for (String userName : cliSchemaCardView.getSchemaCards().keySet()) {
            bufferManager.consolePrint(buildGraphic.buildMessage("Player: " + userName).
                    buildMessage(cliSchemaCardView.getSchemaCard(userName).toString()).
                    buildMessage("").toString(), Level.LOW);
        }
    }

    private void viewPublicObjectiveCards(){
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.buildGraphicPublicObjectiveCards(publicObjectiveCards).
                toString(), Level.LOW);
    }

    private void viewToolCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCards).toString(), Level.LOW);
    }

    private void viewRoundTrack(){
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.
                buildGraphicRoundTrack(cliRoundTrackView.getRoundTrack()).toString(),Level.LOW);
    }

    private void viewDraftPool() {
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.
                buildMessage("---------------------------DRAFT POOL---------------------------").
                buildGraphicDices(cliDraftPoolView.getDraftPool().getDices()).
                toString(),Level.LOW);
    }

    private Position selectPosition() {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        Position position = null;

        int row;
        int column;

        bufferManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").toString(),
                Level.LOW);
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
                    position = new Position(row,column);
                } else {
                    bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                    row = -1;
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                row = -1;
            }
        } while (row < 0);
        return position;
    }

    private void placeDice(){
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int diceNumber;

        bufferManager.consolePrint(buildGraphic.
                buildMessage("---------------------------DRAFT POOL---------------------------").
                buildGraphicDices(cliDraftPoolView.getDraftPool().getDices()).
                buildMessage("\n---------------------------SCHEMA CARD---------------------------").
                buildMessage(cliSchemaCardView.getSchemaCard(currentUser.getName()).toString()).
                toString(),Level.LOW);
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
                            gameName,cliDraftPoolView.getDraftPool().getDices().get(diceNumber - 1),
                            selectPosition());
                } catch (RemoteException e) {
                    bufferManager.consolePrint("NETWORK ERROR", Level.HIGH);
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                diceNumber = -1;
            }
        } while (diceNumber < 0);

    }

    private void playToolCard() throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int number;

        bufferManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCards).toString(), Level.LOW);
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
                        new CLIToolCardView(this,toolCards.get(number - 1)));
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
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
            bufferManager.consolePrint(buildGraphic.toString(), Level.LOW);
            cliSchemaCardView.addSchemaCard(p.getUser().getName(),p.getSchemaCard());
        }
    }

    @Override
    public void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    @Override
    public void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards){
        //...
    }

    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) {
        this.privateObjectiveCards = privateObjectiveCards;
    }

    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards) throws RemoteException {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<SchemaCard> schemaCardList = new ArrayList<>();

        for (List<SchemaCard> ls: schemaCards)
            schemaCardList.addAll(ls);

        buildGraphic.buildMessage("-----------------------------SCHEMA CARDS-----------------------------");
        for (int i = 0; i < schemaCardList.size(); i++) {
            buildGraphic.buildMessage("                   [").buildMessage(String.valueOf(i)).
                    buildMessage("]").buildMessage(schemaCards.get(i).toString() + "\n");
        }

        String response;
        int number;

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
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
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
    public void ack(String ack){
        bufferManager.consolePrint("ACK: " + ack, Level.HIGH);
    }

    @Override
    public void err(String err){
        bufferManager.consolePrint("ERROR: " + err, Level.HIGH);
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

    @Override
    public void notifyModelSynch(DraftPool draftPool, List<Player> players, RoundTrack roundTrack, List<ToolCard> toolCards) {
        // TODO
    }
}
