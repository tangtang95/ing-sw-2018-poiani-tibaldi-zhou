package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.rmi.RemoteException;

/**
 * The CLI of round, with the commands that the player can use when
 * he is not in his turn of play.
 */
public class CLIRoundScreen extends CLIBasicScreen {

    protected final String gameName;
    protected final transient UserWrapper myUser;
    protected final String token;

    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";
    private static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIRoundScreen(ConnectionManager networkManager, ScreenManager screenManager, String gameName,
                          UserWrapper myUser, String token) throws RemoteException {
        super(networkManager, screenManager);
        this.token = token;
        this.myUser = myUser;
        this.gameName = gameName;

        initializeCommands();
    }


    @Override
    protected void initializeCommands() {
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

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(screenManager::popScreen);
        commandMap.put(quit.getCommandText(), quit);
    }

    @Override
    public void startCLI() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                        buildGraphicHelp(commandMap).
                        buildMessage("Choose the action: ").toString(),
                Level.STANDARD);
    }
    private void viewPrivateObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
    }

    private void viewMySchemaCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        //TODO
    }

    private void viewSchemaCards() {
        BuildGraphic buildGraphic = new BuildGraphic();

    }

    private void viewPublicObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        //TODO
    }

    private void viewToolCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        //TODO
    }

    private void viewRoundTrack() {
        BuildGraphic buildGraphic = new BuildGraphic();
        //TODO
    }

    private void viewDraftPool() {
        BuildGraphic buildGraphic = new BuildGraphic();
        //TODO
    }

}
