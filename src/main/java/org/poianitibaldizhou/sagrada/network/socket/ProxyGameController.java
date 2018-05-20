package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.network.socket.messages.GameRequest;

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


    /**
     * Player GameRequest to join a game
     * @param view player's view
     * @param token player's token
     * @param gameName name of the game that player want to join
     */
    @Override
    public void joinGame(IGameView view, String token, String gameName) {
        serverHandler.addViewToHashMap(view.hashCode(), view);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, view, token, gameName));
    }

    /**
     * Sends a dice previously requested by the server.
     *
     * @param dice dice sent
     * @param gameName name of the game in which the player acts
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setDice(Dice dice, String gameName, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, dice, gameName, toolCardName));
    }

    /**
     * Sends an integer value previously requested by the server.
     *
     * @param value value sent
     * @param gameName name of the game in which the player acts
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setNewValue(int value, String gameName, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, value, gameName, toolCardName));
    }

    /**
     * Sends a color previously requested by the server.
     *
     * @param color Color sent
     * @param gameName name of the game in which the player acts
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setColor(Color color, String gameName, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, color, gameName, toolCardName));
    }

    /**
     * Sends a position previously requested by the server.
     *
     * @param position position sent
     * @param gameName name of the game in which the player acts
     * @param toolCardName ToolCard's name that requested this effect
     */
    @Override
    public void setPosition(Position position, String gameName, String toolCardName) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new GameRequest(methodName, position, gameName, toolCardName));
    }
}
