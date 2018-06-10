package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * The CLI of start game, when the game is initialized.
 * In this state there are not any commands.
 */
public class CLISetupGameScreen extends CLIBasicScreen {

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Reference to CLIGameView for initializing the game.
     */
    private final transient CLIGameView cliGameView;

    /**
     * Reference at the player's token.
     */
    private final String token;

    /**
     * Reference at the game name of the current game.
     */
    private final String gameName;

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    private final transient ClientCreateMessage clientCreateMessage = new ClientCreateMessage();

    /**
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameName name of current game.
     * @param myUser my user in the the current game.
     * @param token my token in the current game.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLISetupGameScreen(ConnectionManager connectionManager, ScreenManager screenManager,
                          String gameName, UserWrapper myUser, String token
    ) throws RemoteException {
        super(connectionManager, screenManager);
        this.cliStateView = new CLIStateView(connectionManager,screenManager,gameName,myUser,token);

        this.token = token;
        this.gameName = gameName;

        this.cliGameView = new CLIGameView(cliStateView, connectionManager);
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
    @Override
    protected void initializeCommands() {
        /* For this CLI there is not any command. */
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        pauseCLI();
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
        try {
            connectionManager.getGameController().joinGame(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).
                            buildMessage(),
                    cliGameView,
                    cliGameView,
                    new CLIRoundTrackView(cliStateView),
                    cliStateView,
                    new CLIDraftPoolView(cliStateView),
                    new CLIDiceBagView(cliStateView),
                    new CLITimeoutView(cliStateView)
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLISetUpGame is the same or the token is the same,the game name is the same,
     * the cliStateView is the same and the cliGameView is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISetupGameScreen)) return false;
        if (!super.equals(o)) return false;
        CLISetupGameScreen that = (CLISetupGameScreen) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(cliGameView, that.cliGameView) &&
                Objects.equals(token, that.token) &&
                Objects.equals(gameName, that.gameName);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cliStateView, cliGameView, token, gameName, clientCreateMessage);
    }
}
