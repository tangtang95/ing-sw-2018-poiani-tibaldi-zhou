package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.view.listener.TimeoutListener;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
                                      IDrawableCollectionObserver diceBagObserver, TimeoutListener timeoutListener) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();

        connectionManager.getGameController().joinGame(request, view, gameObserver,
                roundTrackObserver, stateObserver, draftPoolObserver, diceBagObserver, timeoutListener);

        request = builder.createTokenMessage(token)
                .createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response;
        try {
            response = connectionManager.getGameController().getListOfUser(request);
            // TODO HANDLE WITH HAS TERMINATE GAME ERROR
            System.out.println(response);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Network error");
            throw e;
        }
        ClientGetMessage parser = new ClientGetMessage();
        return parser.getListOfUserWrapper(response);
    }

    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token)
                .createGameNameMessage(gameModel.getGameName()).createSchemaCardMessage(schemaCardWrapper).buildMessage();
        connectionManager.getGameController().chooseSchemaCard(request);
    }


    public String getUsername() {
        return username;
    }

    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getSchemaCards(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getSchemaCards(response);
    }

    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getPrivateObjectiveCardByToken(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getPrivateObjectiveCards(response);
    }

    public RoundTrackWrapper getRoundTrack() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getRoundTrack(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getRoundTrack(response);
    }

    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createUsernameMessage(user.getUsername()).buildMessage();
        connectionManager.getGameController().bindPlayer(request, playerObserver, schemaCardObserver);
    }

    public DraftPoolWrapper getDraftPool() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getDraftPool(request);
        return parser.getDraftPool(response);
    }

    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createToolCardMessage(toolCard).buildMessage();
        connectionManager.getGameController().bindToolCard(request, toolCardObserver);
    }

    public void endTurn() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createActionMessage(new EndTurnStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(request);
    }

    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getPlayersCoins(request);
        return parser.getPlayersCoins(response);
    }

    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getSchemaCardByToken(request);
        return parser.getSchemaCard(response);

    }

    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createActionMessage(new PlaceDiceStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String placeDiceRequest = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createDiceMessage(dice).createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().placeDice(placeDiceRequest);
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getPublicObjectiveCards(request);
        return parser.getPublicObjectiveCards(response);
    }

    public List<ToolCardWrapper> getToolCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getToolCards(request);
        return parser.getToolCards(response);
    }

    public List<UserWrapper> getUserList() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response = connectionManager.getGameController().getListOfUser(request);
        return parser.getListOfUserWrapper(response);
    }

    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createActionMessage(new UseToolCardStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String useToolCardRequest = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createToolCardMessage(toolCardWrapper).buildMessage();
        connectionManager.getGameController().useToolCard(useToolCardRequest, executorObserver);
    }

    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createDiceMessage(diceWrapper).buildMessage();
        connectionManager.getGameController().setDice(request);
    }

    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createColorMessage(colorWrapper).buildMessage();
        connectionManager.getGameController().setColor(request);
    }

    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().setPosition(request);
    }

    public void sendAnswerObject(boolean answer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createBooleanMessage(answer).buildMessage();
        connectionManager.getGameController().setContinueAction(request);
    }

    public void sendValueObject(int value) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createValueMessage(value).buildMessage();
        connectionManager.getGameController().setNewValue(request);
    }

    public int getOwnToken() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName()).buildMessage();
        String response =  connectionManager.getGameController().getMyCoins(request);
        return parser.getMyCoins(response);
    }

    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameModel.getGameName())
                .createToolCardMessage(toolCardWrapper).buildMessage();
        String response =  connectionManager.getGameController().getToolCardByName(request);
        return parser.getToolCard(response);
    }
}
