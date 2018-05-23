package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIGameView extends CLIMenuView implements IGameView{
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private List<ToolCard> toolCards;
    private List<PublicObjectiveCard> publicObjectiveCards;
    private User currentUser;
    private String gameName;

    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFTPOOL = "View the Draft Pool";
    private static final String VIEW_ROUNDTRACK = "View the Round Track";
    private static final String VIEW_TOOLCARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMACARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";


    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super(networkManager, screenManager, bufferManager);

     initializeCommands();
    }


    private void initializeCommands() {
        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(screenManager::popScreen);
        commandMap.put(quit.getCommandText(), quit);

    }

    @Override
    public void run() {
        bufferManager.consolePrint("-----------------------------WELCOME-------------------------------",
                Level.LOW);


    }

    private void placeDice(){

    }

    private void playToolCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        String response;
        int number;

        bufferManager.consolePrint(buildGraphic.buidGraphicToolCards(toolCards).toString(), Level.HIGH);
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

    }

    @Override
    public void onToolCardsDraw(List<ToolCard> toolCards) {

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
}
