package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.game.view.IGameView;
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

/**
 * Contains the values that are used in the game session. Furthermore, it makes requests to the server
 * to get or pass information.
 */
public class GameModel {

    private ConnectionManager connectionManager;
    private final String username;
    private final String token;
    private final String gameName;

    private static final String GAME_TERMINATED_ERROR_MESSAGE = "Game terminated error";

    /**
     * Constructor.
     * Create a game model that contains username, token, gameName and connection manager; It makes request
     * to the server to get or pass information
     *
     * @param username the username of the client
     * @param token the token of the client
     * @param gameName the name of the game that the client is playing
     * @param connectionManager the manager of the connection
     */
    public GameModel(String username, String token, String gameName, ConnectionManager connectionManager) {
        this.username = username;
        this.token = token;
        this.gameName = gameName;
        this.connectionManager = connectionManager;
    }

    // GETTER

    /**
     * @return name of the game
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @return user name of the user playing with this client
     */
    public String getUsername() {
        return username;
    }

    /**
     * JoinGame request to the server
     *
     * @param view the game view
     * @param gameObserver the game observer, client side that updates the view
     * @param stateObserver the stateObserver observer, client side that updates the view
     * @param roundTrackObserver the roundTrack observer, client side that updates the view
     * @param draftPoolObserver the draftPool observer, client side that updates the view
     * @param diceBagObserver the dicebag observer, client side that updates the view
     * @param timeOutObserver the timeout observer, client side that updates the view
     * @return the list of user that is playing in the game (name of the game)
     * @throws IOException if network error or the game is terminated
     */
    public List<UserWrapper> joinGame(IGameView view, IGameObserver gameObserver, IStateObserver stateObserver,
                                      IRoundTrackObserver roundTrackObserver, IDraftPoolObserver draftPoolObserver,
                                      IDrawableCollectionObserver diceBagObserver, ITimeOutObserver timeOutObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();

        connectionManager.getGameController().joinGame(request, view, gameObserver,
                roundTrackObserver, stateObserver, draftPoolObserver, diceBagObserver, timeOutObserver);

        request = builder.createTokenMessage(token)
                .createGameNameMessage(gameName).buildMessage();
        String response;
        response = connectionManager.getGameController().getListOfUser(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getListOfUserWrapper(response);
    }

    /**
     * ChooseSchemaCard request to the server: send the schema card chosen to the server
     *
     * @param schemaCardWrapper the schema card chosen
     * @throws IOException network error
     */
    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token)
                .createGameNameMessage(gameName).createSchemaCardMessage(schemaCardWrapper).buildMessage();
        connectionManager.getGameController().chooseSchemaCard(request);
    }

    /**
     * GetSchemaCardMap request to the server
     *
     * @return a map of UserWrapper to SchemaCardWrapper that contains every schema card in the game
     * @throws IOException if network error or the game is terminated
     */
    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getSchemaCards(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getSchemaCards(response);
    }

    /**
     * GetOwnPrivateObjectiveCard request to the server
     *
     * @return the list of private objective cards owned by the user (this.username)
     * @throws IOException if network error or the game is terminated
     */
    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPrivateObjectiveCardByToken(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getPrivateObjectiveCards(response);
    }

    /**
     * GetRoundTrack request to the server
     *
     * @return the round track of the game
     * @throws IOException if network error or the game is terminated
     */
    public RoundTrackWrapper getRoundTrack() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getRoundTrack(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getRoundTrack(response);
    }

    /**
     * BindPlayer request to the server: send the player observer and schemaCardObserver linked to the user given
     *
     * @param user the user to link player observer and schemaCardObserver
     * @param playerObserver the client player observer
     * @param schemaCardObserver the client schemaCard observer
     * @throws IOException network error
     */
    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createUsernameMessage(user.getUsername()).buildMessage();
        connectionManager.getGameController().bindPlayer(request, playerObserver, schemaCardObserver);
    }

    /**
     * GetDraftPool request to the server
     *
     * @return the draft pool of the game
     * @throws IOException if network error or the game is terminated
     */
    public DraftPoolWrapper getDraftPool() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getDraftPool(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getDraftPool(response);
    }

    /**
     * BindToolCard request to the server: send the toolCardObserver that needs to be linked to the tool card given
     * at the server side
     *
     * @param toolCard the copy tool card to be linked
     * @param toolCardObserver the client tool card observer
     * @throws IOException network error
     */
    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCard).buildMessage();
        connectionManager.getGameController().bindToolCard(request, toolCardObserver);
    }

    /**
     * EndTurn request to the server
     *
     * @throws IOException network error
     */
    public void endTurn() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new EndTurnActionWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(request);
    }

    /**
     * GetCoinsMap request to the server
     *
     * @return a map of UserWrapper and Integer that contains the favor tokens of each player
     * @throws IOException if network error or the game is terminated
     */
    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPlayersCoins(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getPlayersCoins(response);
    }

    /**
     * Get the schema card of the player who is playing with this client
     *
     * @return schema card of the player who is playing with this client
     * @throws IOException network communication error or the game is terminated
     */
    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getSchemaCardByToken(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getSchemaCard(response);

    }

    /**
     * PlaceDice request to the server
     *
     * @param dice the dice to place
     * @param positionWrapper the position on which the dice should be placed
     * @throws IOException network error
     */
    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new PlaceDiceActionWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String placeDiceRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createDiceMessage(dice).createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().placeDice(placeDiceRequest);
    }

    /**
     * GetPublicObjectiveCards request to the server
     *
     * @return the list of public objective cards in the game
     * @throws IOException if network error or the game is terminated
     */
    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getPublicObjectiveCards(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getPublicObjectiveCards(response);
    }

    /**
     * GetToolCards request to the server
     *
     * @return the list of tool cards in the game
     * @throws IOException if network error or the game is terminated
     */
    public List<ToolCardWrapper> getToolCards() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getToolCards(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getToolCards(response);
    }

    /**
     * GetUserList request to the server
     *
     * @return the list of users in the game
     * @throws IOException if network error or the game is terminated
     */
    public List<UserWrapper> getUserList() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getListOfUser(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getListOfUserWrapper(response);
    }

    /**
     * UseToolCard request to the server
     *
     * @param toolCardWrapper the toolCard to use
     * @param executorObserver the toolcard executor observer that receive the notify of the server
     * @throws IOException network error
     */
    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String changeActionRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createActionMessage(new UseToolCardActionWrapper()).buildMessage();
        connectionManager.getGameController().chooseAction(changeActionRequest);
        String useToolCardRequest = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        connectionManager.getGameController().useToolCard(useToolCardRequest, executorObserver);
    }

    /**
     * SendDiceObject request to the server
     *
     * @param diceWrapper the dice to send
     * @throws IOException network error
     */
    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createDiceMessage(diceWrapper).buildMessage();
        connectionManager.getGameController().setDice(request);
    }

    /**
     * SendColorObject request to the server
     *
     * @param colorWrapper the color to send
     * @throws IOException network error
     */
    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createColorMessage(colorWrapper).buildMessage();
        connectionManager.getGameController().setColor(request);
    }

    /**
     * SendPositionObject request to the server
     *
     * @param positionWrapper the position to send
     * @throws IOException network error
     */
    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createPositionMessage(positionWrapper).buildMessage();
        connectionManager.getGameController().setPosition(request);
    }

    /**
     * SendAnswerObject request to the server
     *
     * @param answer the binary answer to send
     * @throws IOException network error
     */
    public void sendAnswerObject(boolean answer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createBooleanMessage(answer).buildMessage();
        connectionManager.getGameController().setContinueAction(request);
    }

    /**
     * SendValueObject request to the server
     *
     * @param value the integer value to send
     * @throws IOException network error
     */
    public void sendValueObject(int value) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createValueMessage(value).buildMessage();
        connectionManager.getGameController().setNewValue(request);
    }

    /**
     * GetOwnToken request to the server
     *
     * @return the number of token owned by the client (this.username)
     * @throws IOException if network error or the game is terminated
     */
    public int getOwnToken() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response =  connectionManager.getGameController().getMyCoins(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getMyCoins(response);
    }

    /**
     * GetToolCardByName request to the server
     *
     * @param toolCardWrapper the toolcard requested
     * @return the updated tool card (token updated)
     * @throws IOException if network error or the game is terminated
     */
    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName)
                .createToolCardMessage(toolCardWrapper).buildMessage();
        String response =  connectionManager.getGameController().getToolCardByName(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        return parser.getToolCard(response);
    }

    /**
     * ChoosePrivateObjectiveCard request to the server: select the private objective card
     *
     * @param privateObjectiveCardWrapper the private objective card chosen
     * @throws IOException network error
     */
    public void choosePrivateObjectiveCard(PrivateObjectiveCardWrapper privateObjectiveCardWrapper) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token)
                .createGameNameMessage(gameName).createPrivateObjectiveCardMessage(privateObjectiveCardWrapper).buildMessage();
        connectionManager.getGameController().choosePrivateObjectiveCard(request);
    }

    /**
     * QuitGame request to the server
     *
     * @throws IOException network error
     */
    public void quitGame() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        connectionManager.getGameController().quitGame(request);
    }

    /**
     * GetMillisTimeout request to the server
     *
     * @return the timeout remaining value in millis
     * @throws IOException if network error or the game is terminated
     */
    public long getMillisTimeout() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createTokenMessage(token).createGameNameMessage(gameName).buildMessage();
        String response = connectionManager.getGameController().getTimeout(request);
        if(parser.hasTerminateGameError(response))
            throw new IOException(GAME_TERMINATED_ERROR_MESSAGE);
        String timeoutText = parser.getTimeout(response);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date dt = formatter.parse(timeoutText);
            cal.setTime(dt);
        }catch (ParseException ex){
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.toString());
        }
        long minute = cal.get(Calendar.MINUTE);
        long second = cal.get(Calendar.SECOND);
        return  (minute*60 + second)*1000;
    }
}
