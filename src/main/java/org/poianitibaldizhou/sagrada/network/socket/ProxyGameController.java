package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.network.socket.messages.GameRequest;

import java.rmi.RemoteException;

@Deprecated
public class ProxyGameController extends ProxyController implements IGameController{


    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param serverHandler the serverHandler connected with the server
     */
    public ProxyGameController(ServerHandler serverHandler) {
        super(serverHandler);
    }


    @Override
    public void joinGame(String token, String gameName, IGameView view, IGameObserver gameObserver,
                         IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                         IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver<Dice> diceBagObserver)
            throws RemoteException {
        serverHandler.addViewToHashMap(view.hashCode(), view);
        serverHandler.addViewToHashMap(gameObserver.hashCode(), gameObserver);
        serverHandler.addViewToHashMap(roundTrackObserver.hashCode(), roundTrackObserver);
        serverHandler.addViewToHashMap(stateObserver.hashCode(), stateObserver);
        serverHandler.addViewToHashMap(draftPoolObserver.hashCode(), draftPoolObserver);
        serverHandler.addViewToHashMap(diceBagObserver.hashCode(), diceBagObserver);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, token, gameName, view, gameObserver));
    }

    @Override
    public void chooseSchemaCard(String token, String gameName, SchemaCard schemaCard) throws RemoteException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, token, gameName, schemaCard));
    }

    @Override
    public void bindPlayer(String token, String gameName, Player player, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws RemoteException {

    }

    @Override
    public void bindToolCard(String token, String gameName, ToolCard toolCard, IToolCardObserver toolCardObserver) throws RemoteException {

    }

    @Override
    public void chooseAction(String token, String gameName, IActionCommand actionCommand) throws RemoteException {

    }

    @Override
    public void placeDice(String token, String gameName, Dice dice, Position position) throws RemoteException {

    }

    @Override
    public void useToolCard(String token, String gameName, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws RemoteException {

    }

    @Override
    public void choosePrivateObjectiveCard(String token, String gameName, PrivateObjectiveCard privateObjectiveCard) throws RemoteException {

    }

    /**
     * Sends a dice previously requested by the server.
     *  @param token
     * @param gameName name of the game in which the player acts
     * @param dice dice sent
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setDice(String token, String gameName, Dice dice, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, dice, gameName, toolCardName));
    }

    /**
     * Sends an integer value previously requested by the server.
     *  @param token
     * @param gameName name of the game in which the player acts
     * @param value value sent
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setNewValue(String token, String gameName, int value, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, value, gameName, toolCardName));
    }

    /**
     * Sends a color previously requested by the server.
     *  @param token
     * @param gameName name of the game in which the player acts
     * @param color Color sent
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setColor(String token, String gameName, Color color, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, color, gameName, toolCardName));
    }

    /**
     * Sends a position previously requested by the server.
     *  @param token
     * @param gameName name of the game in which the player acts
     * @param position position sent
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setPosition(String token, String gameName, Position position, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, position, gameName, toolCardName));
    }
}
