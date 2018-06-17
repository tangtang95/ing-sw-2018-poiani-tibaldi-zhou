package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.view.listener.TimeoutListener;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameModel {

    private ConnectionManager connectionManager;
    private final String username;
    private final String token;
    private final String gameName;

    public GameModel(String username, String token, String gameName, ConnectionManager connectionManager) {
        this.username = username;
        this.token = token;
        this.gameName = gameName;
        this.connectionManager = connectionManager;
    }

    public String getGameName() {
        return gameName;
    }

    public String getUsername() {
        return username;
    }

    public List<UserWrapper> joinGame(IGameView view, IGameObserver gameObserver, IStateObserver stateObserver,
                                      IRoundTrackObserver roundTrackObserver, IDraftPoolObserver draftPoolObserver,
                                      IDrawableCollectionObserver diceBagObserver, TimeoutListener timeoutListener) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();

        connectionManager.getGameController().joinGame(request, view, gameObserver,
                roundTrackObserver, stateObserver, draftPoolObserver, diceBagObserver, timeoutListener);

        request = builder.createTokenMessage(token)
                .createGameNameMessage(gameName).buildMessage();
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
                .createGameNameMessage(gameName).createSchemaCardMessage(schemaCardWrapper).buildMessage();
        connectionManager.getGameController().chooseSchemaCard(request);
    }


    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getSchemaCards(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getSchemaCards(response);
    }

    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPrivateObjectiveCardByToken(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getPrivateObjectiveCards(response);
    }

    public RoundTrackWrapper getRoundTrack() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getRoundTrack(request);
        // TODO HANDLE TERMINATE ERROR GAME
        return parser.getRoundTrack(response);
    }

    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createUsernameMessage(user.getUsername()).buildMessage();
        connectionManager.getGameController().bindPlayer(request, playerObserver, schemaCardObserver);
    }

    public DraftPoolWrapper getDraftPool() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getDraftPool(request);
        return parser.getDraftPool(response);
    }

    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCard).buildMessage();
        connectionManager.getGameController().bindToolCard(request, toolCardObserver);
    }

    public void endTurn() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(request);
    }

    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPlayersCoins(request);
        return parser.getPlayersCoins(response);
    }

    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getSchemaCardByToken(request);
        return parser.getSchemaCard(response);

    }

    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new PlaceDiceStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String placeDiceRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createDiceMessage(dice).createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().placeDice(placeDiceRequest);
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPublicObjectiveCards(request);
        return parser.getPublicObjectiveCards(response);
    }

    public List<ToolCardWrapper> getToolCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getToolCards(request);
        return parser.getToolCards(response);
    }

    public List<UserWrapper> getUserList() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getListOfUser(request);
        return parser.getListOfUserWrapper(response);
    }

    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new UseToolCardStateWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String useToolCardRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        connectionManager.getGameController().useToolCard(useToolCardRequest, executorObserver);
    }

    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createDiceMessage(diceWrapper).buildMessage();
        connectionManager.getGameController().setDice(request);
    }

    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createColorMessage(colorWrapper).buildMessage();
        connectionManager.getGameController().setColor(request);
    }

    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().setPosition(request);
    }

    public void sendAnswerObject(boolean answer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createBooleanMessage(answer).buildMessage();
        connectionManager.getGameController().setContinueAction(request);
    }

    public void sendValueObject(int value) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createValueMessage(value).buildMessage();
        connectionManager.getGameController().setNewValue(request);
    }

    public int getOwnToken() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response =  connectionManager.getGameController().getMyCoins(request);
        return parser.getMyCoins(response);
    }

    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        String response =  connectionManager.getGameController().getToolCardByName(request);
        return parser.getToolCard(response);
    }

    public void choosePrivateObjectiveCard(PrivateObjectiveCardWrapper privateObjectiveCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token)
                .createGameNameMessage(gameName).createPrivateObjectiveCardMessage(privateObjectiveCardWrapper).buildMessage();
        connectionManager.getGameController().choosePrivateObjectiveCard(request);
    }

    public void quitGame() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        connectionManager.getGameController().quitGame(request);
        connectionManager.close();
    }

    public long getMillisTimeout() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getTimeout(request);
        String timeoutText = parser.getTimeout(response);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date dt = formatter.parse(timeoutText);
            cal.setTime(dt);
        }catch (ParseException ex){
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.toString());
        }
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return  (minute*60 + second)*1000;
    }
}
