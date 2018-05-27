package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIGameView extends CLIMenuView implements IGameView{
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private transient List<ToolCard> toolCards;
    private transient List<PublicObjectiveCard> publicObjectiveCards;
    private transient User currentUser;
    private String gameName;

    private final CLISchemaCardView cliSchemaCardView;
    private final CLIDraftPoolView cliDraftPoolView;
    private final CLIStateView cliStateView;
    private final CLIRoundTrackView cliRoundTrackView;

    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";


    public CLIGameView(ConnectionManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super(networkManager, screenManager, bufferManager);

        this.cliSchemaCardView = new CLISchemaCardView(this);
        this.cliDraftPoolView = new CLIDraftPoolView(this);
        this.cliStateView = new CLIStateView(this);
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

    }

    @Override
    public void run() {
        bufferManager.consolePrint("-----------------------------WELCOME-------------------------------",
                Level.LOW);


    }

    private void viewMySchemaCard() {
        bufferManager.consolePrint("", Level.LOW);
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
                //TODO...
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                diceNumber = -1;
            }
        } while (diceNumber < 0);

    }

    private void playToolCard() {
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
                try {
                    new CLIToolCardView(this,toolCards.get(number - 1));
                } catch (RemoteException e) {
                    bufferManager.consolePrint("NETWORK ERROR", Level.HIGH);
                }
            } else {
                bufferManager.consolePrint(NUMBER_WARNING, Level.LOW);
                number = -1;
            }
        } while (number < 0);
    }

    @Override
    public void onPlayersCreate(List<Player> players) {

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

    }

    @Override
    public void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) {

    }

    @Override
    public void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards) {

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
    public void ack(String ack) throws RemoteException {

    }

    @Override
    public void err(String err) throws RemoteException {

    }
}
