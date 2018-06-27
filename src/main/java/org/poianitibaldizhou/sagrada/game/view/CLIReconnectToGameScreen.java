package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.*;

/**
 * CLIReconnectToGameScreen for reconnect the player to his game.
 */
public class CLIReconnectToGameScreen extends CLIBasicScreen {

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    private final transient ClientCreateMessage clientCreateMessage;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Reference at the player's token.
     */
    private String token = null;

    /**
     * Reference to my username in game.
     */
    private String username = null;

    /**
     * Reference at the game name of the current game.
     */
    private String gameName = null;

    /**
     * List of user in game.
     */
    private transient List<UserWrapper> userList = null;

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIReconnectToGameScreen(ConnectionManager connectionManager, ScreenManager screenManager) throws RemoteException {
        super(connectionManager,screenManager);
        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();

        getParameter();

        this.cliStateView = new CLIStateView(connectionManager,screenManager,
                new CLIMultiPlayerScreen(connectionManager, screenManager,gameName, token),
                new UserWrapper(username));

        initializeCommands();
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
    @Override
    protected void initializeCommands() {
        /* For this CLI there are not any commands. */
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
        CLIBasicScreen.clearScreen();
        PrinterManager.consolePrint(ClientMessage.RECONNECT_TO_YOUR_GAME, Level.INFORMATION);
        if (userList != null && token != null && gameName != null) {
            try {
                Map<String, IPlayerObserver> cliPlayerViewMap = new HashMap<>();
                Map<String, IToolCardObserver> cliToolCardViewMap = new HashMap<>();
                Map<String, ISchemaCardObserver> cliSchemaCardViewMap = new HashMap<>();

                String response = connectionManager.getGameController().getToolCards(
                        clientCreateMessage.createGameNameMessage(gameName).
                                createTokenMessage(token).buildMessage()
                );
                if(clientGetMessage.hasTerminateGameError(response))
                    return;
                List<ToolCardWrapper> toolCardWrappers = clientGetMessage.getToolCards(response);

                response = connectionManager.getGameController().getSchemaCards(
                        clientCreateMessage.createGameNameMessage(gameName).
                                createTokenMessage(token).buildMessage()
                );
                if(clientGetMessage.hasTerminateGameError(response))
                    return;
                Map<UserWrapper,SchemaCardWrapper> schemaCardWrappers = clientGetMessage.getSchemaCards(response);

                for (UserWrapper u : userList)
                    cliPlayerViewMap.put(u.getUsername(), new CLIPlayerView(cliStateView, u.getUsername()));

                for (ToolCardWrapper t : toolCardWrappers)
                    cliToolCardViewMap.put(t.getName(), new CLIToolCardView(cliStateView, t.getName()));

                for (Map.Entry<UserWrapper,SchemaCardWrapper> s : schemaCardWrappers.entrySet())
                    cliSchemaCardViewMap.put(s.getKey().getUsername(), new CLISchemaCardView(cliStateView, s.getValue().getName()));

                connectionManager.getGameController().reconnect(
                        clientCreateMessage.createTokenMessage(token).createUsernameMessage(username).buildMessage(),
                        new CLIGameView(cliStateView, connectionManager),
                        cliStateView,
                        cliPlayerViewMap,
                        cliToolCardViewMap,
                        cliSchemaCardViewMap,
                        new CLIGameView(cliStateView, connectionManager),
                        new CLIDraftPoolView(cliStateView),
                        new CLIRoundTrackView(cliStateView),
                        new CLIDiceBagView(cliStateView),
                        new CLITimeoutView(cliStateView)
                );

                screenManager.replaceScreen(new CLIMultiPlayerScreen(connectionManager, screenManager, gameName, token));
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        ClientMessage.NETWORK_ERROR, Level.ERROR);
                screenManager.popScreen();
            }
        } else {
            PrinterManager.consolePrint(ClientMessage.RECONNECT_FAILED, Level.ERROR);
            screenManager.popScreen();
        }
    }

    /**
     * Gets the parameter necessary for the reconnection.
     */
    private void getParameter()  {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        PrinterManager.consolePrint(ClientMessage.PROVIDE_AN_USERNAME, Level.STANDARD);

        while(username == null) {
            try {
                username = r.readLine();
                if (username.equals(""))
                    throw new IllegalArgumentException();
                else {
                    String response = connectionManager.getGameController().attemptReconnect(clientCreateMessage.
                            createUsernameMessage(username).buildMessage());

                    gameName = clientGetMessage.getGameName(response);
                    token = clientGetMessage.getToken(response);
                    userList = clientGetMessage.getListOfUserWrapper(response);
                }
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        ClientMessage.ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                username = null;
            } catch(NullPointerException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        ClientMessage.NETWORK_ERROR, Level.ERROR);
            }
        }
        consoleListener.wakeUpCommandConsole();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIReconnectToScreen has the same gameName, username, token and userList.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIReconnectToGameScreen)) return false;
        if (!super.equals(o)) return false;
        CLIReconnectToGameScreen that = (CLIReconnectToGameScreen) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(username, that.username) &&
                Objects.equals(gameName, that.gameName) &&
                Objects.equals(userList, that.userList);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),token, username, gameName, userList);
    }
}
