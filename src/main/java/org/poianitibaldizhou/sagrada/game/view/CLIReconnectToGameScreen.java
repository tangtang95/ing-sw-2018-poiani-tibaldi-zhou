package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.List;

public class CLIReconnectToGameScreen implements IScreen {

    private ConnectionManager connectionManager;
    private ScreenManager screenManager;

    private String token = null;
    private String username = null;
    private String gameName = null;
    private List<UserWrapper> userList = null;

    private ClientCreateMessage clientCreateMessage;
    private ClientGetMessage clientGetMessage;

    public CLIReconnectToGameScreen(ConnectionManager connectionManager, ScreenManager screenManager) {
        this.connectionManager = connectionManager;
        this.screenManager = screenManager;

        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();
    }

    @Override
    public void startCLI() {
        getParameter();

        if(userList != null && token != null && gameName != null) {
            try {
                screenManager.replaceScreen(CLIRoundScreen.reconnect());
            } catch (RemoteException e) {
                PrinterManager.consolePrint("Re-connect failed", Level.ERROR);
            }
        }

        screenManager.popScreen();
    }

    /**
     * Gets the parameter necessary for the reconnection.
     */
    private void getParameter()  {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        PrinterManager.consolePrint("Provide an username: \n", Level.STANDARD);

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
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                username = null;
            }
        }
    }
}
