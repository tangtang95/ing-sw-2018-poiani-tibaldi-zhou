package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class CLISetupGameScreen extends CLIBasicScreen {

    private final transient CLIStateView cliStateView;
    private final transient CLIGameView cliGameView;
    private final String token;
    private final String gameName;

    private final transient ClientCreateMessage clientCreateMessage = new ClientCreateMessage();

    public CLISetupGameScreen(ConnectionManager connectionManager, ScreenManager screenManager,
                          String gameName, UserWrapper myUser, String token
    ) throws RemoteException {
        super(connectionManager, screenManager);
        this.cliStateView = new CLIStateView(connectionManager,screenManager,gameName,myUser,token);

        this.token = token;
        this.gameName = gameName;

        this.cliGameView = new CLIGameView(cliStateView, connectionManager);
    }

    @Override
    protected void initializeCommands() {
        /*
        For this CLI there are not any commands.
         */
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISetupGameScreen)) return false;
        if (!super.equals(o)) return false;
        CLISetupGameScreen that = (CLISetupGameScreen) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(cliGameView, that.cliGameView) &&
                Objects.equals(token, that.token) &&
                Objects.equals(gameName, that.gameName) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliStateView, cliGameView, token, gameName, clientCreateMessage);
    }
}
