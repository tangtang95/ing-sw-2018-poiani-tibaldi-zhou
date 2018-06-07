package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
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

    public List<UserWrapper> joinGame(IGameView view, IGameObserver gameObserver, IStateObserver stateObserver,
                                      IRoundTrackObserver roundTrackObserver, IDraftPoolObserver draftPoolObserver,
                                      IDrawableCollectionObserver diceBagObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();

        // TODO add timeout obs
        connectionManager.getGameController().joinGame(request, view, gameObserver,
                roundTrackObserver, stateObserver, draftPoolObserver, diceBagObserver, null);

        request = builder.createTokenMessage(token)
                .createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response;
        try {
            response = connectionManager.getGameController().getListOfUser(request);
            System.out.println(response);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Network error");
            throw e;
        }
        ClientGetMessage parser = new ClientGetMessage();
        return parser.getListOfUserWrapper(response);
    }


    public String getUsername() {
        return username;
    }
}
