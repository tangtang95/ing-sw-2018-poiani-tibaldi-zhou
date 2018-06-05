package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiPlayerModel {

    private ConnectionManager connectionManager;
    private final String username;
    private final String token;

    private GameModel gameModel;

    public MultiPlayerModel(String username, String token, GameModel gameModel, ConnectionManager connectionManager) {
        this.username = username;
        this.token = token;
        this.gameModel = gameModel;
        this.connectionManager = connectionManager;
    }

    public List<UserWrapper> joinGame() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        //connectionManager.getGameController().joinGame(request);

        request = builder.createTokenMessage(token)
                .createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = null;
        try {
            response = connectionManager.getGameController().getListOfUser(request);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Network error");
            throw e;
        }
        ClientGetMessage parser = new ClientGetMessage();
        return parser.getListOfUserWrapper(response);
    }


}
